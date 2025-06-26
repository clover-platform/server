package plus.xyc.server.i18n.entry.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cache.annotation.CacheEvict;
import org.zkit.support.server.ai.api.entity.Document;
import org.zkit.support.server.ai.api.entity.InvokeRequest;
import org.zkit.support.server.ai.api.service.ChatApiService;
import org.zkit.support.server.ai.api.service.VectorStoreApiService;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.activity.entity.enums.ActivityEntryType;
import plus.xyc.server.i18n.activity.entity.enums.ActivityOperate;
import plus.xyc.server.i18n.activity.service.ActivityService;
import plus.xyc.server.i18n.configuration.AppConfiguration;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.entity.dto.EntryState;
import plus.xyc.server.i18n.entry.entity.mapstruct.EntryResultMapStruct;
import plus.xyc.server.i18n.entry.entity.request.EntryAIResultRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryResultListRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryResultSaveRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryResultResponse;
import plus.xyc.server.i18n.entry.mapper.EntryMapper;
import plus.xyc.server.i18n.entry.mapper.EntryResultMapper;
import plus.xyc.server.i18n.entry.mapper.EntryStateMapper;
import plus.xyc.server.i18n.entry.service.EntryResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.entry.service.EntryStateService;
import plus.xyc.server.i18n.common.enums.I18nCode;
import plus.xyc.server.i18n.language.entity.response.LanguageResponse;
import plus.xyc.server.i18n.language.service.LanguageService;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.service.MainAccountApiService;

import java.util.*;

/**
 * <p>
 * 翻译结果 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
@Slf4j
public class EntryResultServiceImpl extends ServiceImpl<EntryResultMapper, EntryResult> implements EntryResultService {

    @Resource
    private EntryResultMapStruct mapStruct;
    @DubboReference
    private MainAccountApiService mainAccountApiService;
    @Resource
    private ActivityService activityService;
    @Resource
    private EntryStateService entryStateService;
    @Resource
    private EntryMapper entryMapper;
    @DubboReference
    private ChatApiService chatApiService;
    @DubboReference
    private VectorStoreApiService vectorStoreApiService;
    @Resource
    private LanguageService languageService;
    @Resource
    private EntryStateMapper entryStateMapper;
    @Resource
    private AppConfiguration configuration;

    @Override
    public List<EntryResult> getLastResults(List<Long> ids, String language) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return baseMapper.getLastResults(ids, language);
    }

    @Override
    public List<EntryResult> getResults(List<Long> ids, String language) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return baseMapper.getResults(ids, language);
    }

    @Override
    public PageResult<EntryResultResponse> query(PageRequest page, EntryResultListRequest request) {
        try(Page<EntryResult> pageResult = page.start()) {
            List<EntryResult> list = baseMapper.query(request);

            List<Long> translatorIds = list.stream().map(EntryResult::getTranslatorId).filter(Objects::nonNull).toList();
            List<Long> verifierIds = list.stream().map(EntryResult::getCheckerId).filter(Objects::nonNull).toList();
            Set<Long> ids = new HashSet<>(translatorIds);
            ids.addAll(verifierIds);
            List<Long> uniqueIds = new ArrayList<>(ids);

            ApiAccountListRequest apiRequest = new ApiAccountListRequest();
            apiRequest.setIds(uniqueIds);
            apiRequest.setSize(uniqueIds.size());
            PageResult<ApiAccountResponse> r =  mainAccountApiService.list(apiRequest);
            List<EntryResultResponse> responses = list.stream().map(result -> {
                EntryResultResponse response = mapStruct.toEntryResultResponse(result);
                response.setTranslator(r.getData().stream().filter(user -> user.getId().equals(result.getTranslatorId())).findFirst().orElse(null));
                response.setVerifier(r.getData().stream().filter(user -> user.getId().equals(result.getCheckerId())).findFirst().orElse(null));
                return response;
            }).toList();
            return PageResult.of(pageResult.getTotal(), responses);
        }
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:entry:result:'+#request.entryId")
    @CacheEvict(value = "i18n:entry:detail", key = "#request.entryId")
    public void saveResult(EntryResultSaveRequest request) {
        EntryResult origin = baseMapper.findOneByEntryIdAndLanguageAndContent(request.getEntryId(), request.getLanguage(), request.getContent());
        if (origin != null) {
            throw new ResultException(I18nCode.ENTRY_RESULT_EXIST.code, MessageUtils.get(I18nCode.ENTRY_RESULT_EXIST.key));
        }
        EntryResult result = new EntryResult();
        result.setEntryId(request.getEntryId());
        result.setLanguage(request.getLanguage());
        result.setContent(request.getContent());
        result.setTranslatorId(request.getUserId());
        this.save(result);

        entryStateService.translate(request.getEntryId(), request.getLanguage(), result.getId());

        // 记录日志
        activityService.entity(request.getModuleId(), ActivityEntryType.TRANSLATE.code, ActivityOperate.ADD.code, result);

        syncDocument(request.getEntryId(), request.getLanguage());
    }

    private void syncDocument(Long entityId, String language) {
        List<EntryState> states = entryStateMapper.findByEntryIdInAndLanguage(List.of(entityId), language);
        List<Long> resultIds = states.stream().map(EntryState::getResultId).toList();
        List<EntryResult> results = this.getResults(resultIds, language);
        log.info("results: {}", results);
        if(results.isEmpty()) { // 删除
            log.info("remove {}", entityId + "-" + language);
            // aiapiService.removeDocuments(List.of(entityId + "-" + language));
            return;
        }
        results.stream().findFirst().ifPresent(last -> {
            log.info("last {}", last);
            Entry entry = entryMapper.selectById(entityId);
            Document document = new Document();
            document.setId(entry.getId().toString() + "-" + language);
            document.setContent("source:["+entry.getValue() + "], result:[" + last.getContent()+"]");
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("source", "i18n");
            metadata.put("language", language);
            document.setMetadata(metadata);
            vectorStoreApiService.add(document);
        });
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:entry:result:'+#id")
    @CacheEvict(value = "i18n:entry:detail", key = "#entryId")
    public void delete(Long entryId, Long id, Long userId) {
        EntryResult result = baseMapper.selectById(id);
        Entry entry = entryMapper.selectById(result.getEntryId());
        result.setDeleted(true);
        result.setUpdateTime(new Date());
        baseMapper.updateById(result);

        entryStateService.removeTranslate(result.getEntryId(), result.getLanguage(), result.getId());

        // 记录日志
        activityService.entity(entry.getModuleId(), ActivityEntryType.TRANSLATE.code, ActivityOperate.DELETE.code, result);

        syncDocument(entryId, result.getLanguage());
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:entry:result:'+#id")
    @CacheEvict(value = "i18n:entry:detail", key = "#entryId")
    public void approve(Long entryId, Long id, Long userId) {
        EntryResult result = baseMapper.selectById(id);
        Entry entry = entryMapper.selectById(result.getEntryId());

        // 不等于 id 的 设置 verified = false
        lambdaUpdate().set(EntryResult::getVerified, false)
                .ne(EntryResult::getId, result.getId())
                .eq(EntryResult::getEntryId, result.getEntryId())
                .eq(EntryResult::getLanguage, result.getLanguage())
                .update();

        // 更新当前
        Date now = new Date();
        lambdaUpdate().set(EntryResult::getCheckerId, userId)
                .set(EntryResult::getVerified, true)
                .set(EntryResult::getVerifiedTime, now)
                .eq(EntryResult::getId, result.getId())
                .update();

        entryStateService.approve(result.getEntryId(), result.getLanguage(), result.getId());

        // 记录日志
        activityService.entity(entry.getModuleId(), ActivityEntryType.TRANSLATE.code, ActivityOperate.APPROVE.code, result);
    }

    @Override
    public List<String> ai(EntryAIResultRequest request) {
        Entry entry = entryMapper.selectById(request.getEntryId());
        LanguageResponse response = languageService.getByCode(request.getLanguage());
        InvokeRequest invokeRequest = new InvokeRequest();
        List<String> rules = new ArrayList<>(configuration.getRules());
        invokeRequest.setUseContext(true);
        invokeRequest.setMessage("请文案 " + entry.getValue() + " 翻译为 " + response.getName());
        invokeRequest.setRules(rules);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", "i18n");
        metadata.put("language", request.getLanguage());
        invokeRequest.setMetadata(metadata);
        invokeRequest.setUseContext(true);
        String result = chatApiService.invoke(invokeRequest);
        log.info("{}", result);
        return JSONArray.parseArray(result, String.class).stream().map(String::trim).distinct().toList();
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:entry:result:'+#id")
    @CacheEvict(value = "i18n:entry:detail", key = "#entryId")
    public void removeApproval(Long entryId, Long id, Long userId) {
        EntryResult result = baseMapper.selectById(id);
        Entry entry = entryMapper.selectById(result.getEntryId());

        lambdaUpdate().set(EntryResult::getVerified, false).eq(EntryResult::getId, result.getId()).update();

        List<Long> ids = List.of(result.getEntryId());
        List<EntryResult> results = getLastResults(ids, result.getLanguage());
        Long lastId = results.get(0).getId();
        entryStateService.removeApproval(result.getEntryId(), result.getLanguage(), lastId);

        activityService.entity(entry.getModuleId(), ActivityEntryType.TRANSLATE.code, ActivityOperate.REMOVE_APPROVAL.code, result);
    }

    @Override
    public List<EntryResult> getResults(List<Long> ids) {
        if(ids == null || ids.isEmpty()) {
            return List.of();
        }
        return lambdaQuery().in(EntryResult::getEntryId, ids).eq(EntryResult::getDeleted, false).list();
    }
}
