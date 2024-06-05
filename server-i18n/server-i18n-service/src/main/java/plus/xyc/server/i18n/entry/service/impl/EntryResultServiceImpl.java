package plus.xyc.server.i18n.entry.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.zkit.support.server.openai.api.entity.TranslatorRequest;
import org.zkit.support.server.openai.api.rest.OpenAIRestApi;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.activity.entity.enums.ActivityEntryType;
import plus.xyc.server.i18n.activity.entity.enums.ActivityOperate;
import plus.xyc.server.i18n.activity.service.ActivityService;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.entity.mapstruct.EntryResultMapStruct;
import plus.xyc.server.i18n.entry.entity.request.EntryAIResultRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryResultListRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryResultSaveRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryResultResponse;
import plus.xyc.server.i18n.entry.mapper.EntryMapper;
import plus.xyc.server.i18n.entry.mapper.EntryResultMapper;
import plus.xyc.server.i18n.entry.service.EntryResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.entry.service.EntryStateService;
import plus.xyc.server.i18n.enums.I18nCode;
import plus.xyc.server.i18n.language.entity.response.LanguageResponse;
import plus.xyc.server.i18n.language.service.LanguageService;
import plus.xyc.server.i18n.member.entity.enums.MemberRoleType;
import plus.xyc.server.i18n.module.service.ModuleAccessService;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.rest.MainAccountRestApi;

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
public class EntryResultServiceImpl extends ServiceImpl<EntryResultMapper, EntryResult> implements EntryResultService {

    @Resource
    private EntryResultMapStruct mapStruct;
    @Resource
    private MainAccountRestApi mainAccountRestApi;
    @Resource
    private ModuleAccessService moduleAccessService;
    @Resource
    private ActivityService activityService;
    @Resource
    private EntryStateService entryStateService;
    @Resource
    private EntryMapper entryMapper;
    @Resource
    private OpenAIRestApi openAIRestApi;
    @Resource
    private LanguageService languageService;

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
    public PageResult<EntryResultResponse> query(PageQueryRequest page, EntryResultListRequest request) {
        Page<EntryResult> pageResult = page.toPage();
        List<EntryResult> list = baseMapper.query(pageResult, request);

        List<Long> translatorIds = list.stream().map(EntryResult::getTranslatorId).filter(Objects::nonNull).toList();
        List<Long> verifierIds = list.stream().map(EntryResult::getCheckerId).filter(Objects::nonNull).toList();
        Set<Long> ids = new HashSet<>(translatorIds);
        ids.addAll(verifierIds);
        List<Long> uniqueIds = new ArrayList<>(ids);
        Result<PageResult<ApiAccountResponse>> r =  mainAccountRestApi.list(uniqueIds, uniqueIds.size(), 1);
        if(!r.isSuccess()) {
            throw ResultException.internal();
        }

        List<EntryResultResponse> responses = list.stream().map(result -> {
            EntryResultResponse response = mapStruct.toEntryResultResponse(result);
            response.setTranslator(r.getData().getData().stream().filter(user -> user.getId().equals(result.getTranslatorId())).findFirst().orElse(null));
            response.setVerifier(r.getData().getData().stream().filter(user -> user.getId().equals(result.getCheckerId())).findFirst().orElse(null));
            return response;
        }).toList();
        return PageResult.of(pageResult.getTotal(), responses);
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:entry:result:'+#request.entryId")
    public void saveResult(EntryResultSaveRequest request) {
        boolean checked = moduleAccessService.check(request.getModuleId(), request.getUserId(), List.of(MemberRoleType.OWNER.code, MemberRoleType.ADMIN.code, MemberRoleType.TRANSLATOR.code));
        if (!checked) {
            throw new ResultException(I18nCode.ACCESS_ERROR.code, MessageUtils.get(I18nCode.ACCESS_ERROR.key));
        }
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
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:entry:result:'+#id")
    public void delete(Long id, Long userId) {
        EntryResult result = baseMapper.selectById(id);
        Entry entry = entryMapper.selectById(result.getEntryId());
        if(result.getTranslatorId().longValue() != userId.longValue()) {
            boolean checked = moduleAccessService.check(entry.getModuleId(), userId, List.of(MemberRoleType.OWNER.code, MemberRoleType.ADMIN.code));
            if(!checked) {
                throw new ResultException(I18nCode.ACCESS_ERROR.code, MessageUtils.get(I18nCode.ACCESS_ERROR.key));
            }
        }
        result.setDeleted(true);
        result.setUpdateTime(new Date());
        baseMapper.updateById(result);

        entryStateService.removeTranslate(result.getEntryId(), result.getLanguage(), result.getId());

        // 记录日志
        activityService.entity(entry.getModuleId(), ActivityEntryType.TRANSLATE.code, ActivityOperate.DELETE.code, result);
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:entry:result:'+#id")
    public void approve(Long id, Long userId) {
        EntryResult result = baseMapper.selectById(id);
        Entry entry = entryMapper.selectById(result.getEntryId());
        boolean checked = moduleAccessService.check(entry.getModuleId(), userId, List.of(MemberRoleType.OWNER.code, MemberRoleType.ADMIN.code, MemberRoleType.CHECK.code));
        if (!checked) {
            throw new ResultException(I18nCode.ACCESS_ERROR.code, MessageUtils.get(I18nCode.ACCESS_ERROR.key));
        }

        // 不等于 id 的 设置 verified = false
        update().set("verified", false).ne("id", result.getId()).eq("entry_id", result.getEntryId()).eq("language", result.getLanguage()).update();

        // 更新当前
        Date now = new Date();
        update().set("checker_id", userId).set("verified", true).set("update_time", now).set("verified_time", now).eq("id", result.getId()).update();

        entryStateService.approve(result.getEntryId(), result.getLanguage(), result.getId());

        // 记录日志
        activityService.entity(entry.getModuleId(), ActivityEntryType.TRANSLATE.code, ActivityOperate.APPROVE.code, result);
    }

    @Override
    public List<String> ai(EntryAIResultRequest request) {
        Entry entry = entryMapper.selectById(request.getEntryId());
        LanguageResponse response = languageService.getByCode(request.getLanguage());
        TranslatorRequest tr = new TranslatorRequest();
        tr.setMessage(entry.getValue());
        tr.setTarget(response.getName());
        return openAIRestApi.translator(tr);
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:entry:result:'+#id")
    public void removeApproval(Long id, Long userId) {
        EntryResult result = baseMapper.selectById(id);
        Entry entry = entryMapper.selectById(result.getEntryId());
        boolean checked = moduleAccessService.check(entry.getModuleId(), userId, List.of(MemberRoleType.OWNER.code, MemberRoleType.ADMIN.code, MemberRoleType.CHECK.code));
        if (!checked) {
            throw new ResultException(I18nCode.ACCESS_ERROR.code, MessageUtils.get(I18nCode.ACCESS_ERROR.key));
        }

        update().set("verified", false).eq("id", result.getId()).update();

        entryStateService.removeApproval(result.getEntryId(), result.getLanguage(), result.getId());

        activityService.entity(entry.getModuleId(), ActivityEntryType.TRANSLATE.code, ActivityOperate.REMOVE_APPROVAL.code, result);
    }
}
