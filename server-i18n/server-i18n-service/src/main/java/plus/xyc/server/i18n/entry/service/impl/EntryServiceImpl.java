package plus.xyc.server.i18n.entry.service.impl;

import com.github.pagehelper.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.activity.entity.enums.ActivityEntryType;
import plus.xyc.server.i18n.activity.entity.enums.ActivityOperate;
import plus.xyc.server.i18n.activity.service.ActivityService;
import plus.xyc.server.i18n.branch.entity.dto.Branch;
import plus.xyc.server.i18n.branch.mapper.BranchMapper;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.entity.dto.EntryState;
import plus.xyc.server.i18n.entry.entity.mapstruct.EntryMapStruct;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCreateRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryEditRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCountResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithResultResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithStateResponse;
import plus.xyc.server.i18n.entry.mapper.EntryMapper;
import plus.xyc.server.i18n.entry.mapper.EntryResultMapper;
import plus.xyc.server.i18n.entry.mapper.EntryStateMapper;
import plus.xyc.server.i18n.entry.service.EntryResultService;
import plus.xyc.server.i18n.entry.service.EntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.entry.service.EntryStateService;
import plus.xyc.server.i18n.common.enums.I18nCode;
import plus.xyc.server.i18n.module.entity.dto.ModuleCount;
import plus.xyc.server.i18n.module.entity.dto.ModuleTargetLanguage;
import plus.xyc.server.i18n.module.mapper.ModuleCountMapper;
import plus.xyc.server.i18n.module.mapper.ModuleTargetLanguageMapper;

import java.util.*;

/**
 * <p>
 * 词条 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
@Slf4j
public class EntryServiceImpl extends ServiceImpl<EntryMapper, Entry> implements EntryService {

    @Resource
    private EntryResultService entryResultService;
    @Resource
    private EntryMapStruct entryMapStruct;
    @Resource
    private EntryResultMapper entryResultMapper;
    @Resource
    private ActivityService activityService;
    @Resource
    private BranchMapper branchMapper;
    @Resource
    private EntryStateMapper entryStateMapper;
    @Resource
    private ModuleCountMapper moduleCountMapper;
    @Resource
    private ModuleTargetLanguageMapper moduleTargetLanguageMapper;
    @Resource
    private EntryStateService entryStateService;

    @Override
    public PageResult<EntryWithStateResponse> query(PageRequest query, EntryListRequest request) {
        try(Page<EntryWithStateResponse> page = query.start()) {
            List<Entry> all = baseMapper.query(query.getKeyword(), request);
            if(all.isEmpty()) {
                return PageResult.of(0, List.of());
            }
            List<Long> entryIds = all.stream().map(Entry::getId).toList();
            List<EntryState> states = entryStateMapper.findByEntryIdInAndLanguage(entryIds, request.getLanguage());
            List<Long> resultIds = states.stream().map(EntryState::getResultId).toList();
            List<EntryResult> results = entryResultService.getResults(resultIds, request.getLanguage());
            List<EntryWithStateResponse> list = all.stream().map(item -> {
                EntryState state = states.stream().filter(entryState -> entryState.getEntryId().equals(item.getId())).findFirst().orElse(null);
                EntryWithStateResponse response = entryMapStruct.toEntryWithStateResponse(item);
                response.setTranslated(state != null && state.getTranslated());
                response.setVerified(state != null && state.getVerified());
                if(state != null && response.getTranslated()) {
                    EntryResult result = results.stream().filter(entryResult -> entryResult.getId().equals(state.getResultId())).findFirst().orElse(null);
                    response.setTranslation(result);
                }
                return response;
            }).toList();
            return PageResult.of(page.getTotal(), list);
        }
    }

    @Override
    public EntryCountResponse count(EntryCountRequest request) {
        EntryCountResponse response = new EntryCountResponse();
        List<ModuleCount> counts = moduleCountMapper.findByCountRequest(request);
        long total = 0;
        long translated = 0;
        long verified = 0;
        for (ModuleCount count : counts) {
            total += count.getTotalEntry();
            translated += count.getTranslatedEntry();
            verified += count.getVerifiedEntry();
        }
        response.setTotal(total);
        response.setTranslated(translated);
        response.setVerified(verified);
        return response;
    }

    @Override
    public List<EntryWithResultResponse> getEntryByBranchIdWithResult(Long branchId) {
        List<Entry> entries = baseMapper.findByBranchId(branchId);
        List<Long> entryIds = entries.stream().map(Entry::getId).toList();
        List<EntryResult> results = entryIds.isEmpty() ? List.of() : entryResultMapper.findByEntryIds(entryIds);
        return entries.stream().map(entry -> {
            List<EntryResult> entryResults = results.stream().filter(entryResult -> entryResult.getEntryId().equals(entry.getId())).toList();
            EntryWithResultResponse response = entryMapStruct.toEntryWithResultResponse(entry);
            response.setResults(entryResults);
            return response;
        }).toList();
    }

    @Override
    public void cloneEntriesBySourceId(Long sourceId, Long targetId) {
        List<EntryWithResultResponse> entries = getEntryByBranchIdWithResult(sourceId);
        cloneEntries(entries, targetId);
    }

    @Override
    public void cloneEntries(List<EntryWithResultResponse> sources, Long targetId) {
        Branch target = branchMapper.selectById(targetId);
        List<ModuleTargetLanguage> languages = moduleTargetLanguageMapper.findByModuleId(target.getModuleId());
        sources.forEach(entry -> {
            entry.setId(null);
            entry.setBranchId(targetId);
            entry.setUpdateTime(new Date());
        });
        List<Entry> all = sources.stream().map(entry -> entryMapStruct.toEntryFromEntryWithResultResponse(entry)).toList();
        // 保存数据
        // 每次插入100条
        for (int i = 0; i < sources.size(); i += 100) {
            List<Entry> part = all.subList(i, Math.min(i + 100, sources.size()));
            EntryService self = (EntryService)AopContext.currentProxy();
            self.saveBatch(part);
        }
        sources.forEach(entry -> {
            entry.setId(Objects.requireNonNull(all.stream().filter(item -> item.getValue().equals(entry.getValue())).findFirst().orElse(null)).getId());
        });

        List<EntryResult> results = sources.stream().map(entry -> entry.getResults().stream().peek(result -> {
            result.setId(null);
            result.setEntryId(entry.getId());
            result.setUpdateTime(new Date());
        }).toList()).toList().stream().flatMap(List::stream).toList();
        // 保存数据
        // 每次插入100条
        for (int i = 0; i < results.size(); i += 100) {
            List<EntryResult> part = results.subList(i, Math.min(i + 100, results.size()));
            entryResultService.saveBatch(part);
        }

        // 重建 state
        List<EntryState> states = new ArrayList<>();
        sources.forEach(entry -> {
            languages.forEach(language -> {
                // language=language.getCode & 按 updateTime 降序排序，获取最后一条
                EntryResult lastResult = entry.getResults().stream().filter(result -> result.getLanguage().equals(language.getCode())).max(Comparator.comparing(EntryResult::getUpdateTime)).orElse(null);

                EntryState state = new EntryState();
                state.setEntryId(entry.getId());
                state.setLanguage(language.getCode());
                state.setTranslated(lastResult != null);
                state.setVerified(lastResult != null ? lastResult.getVerified() : false);
                state.setTranslationTime(lastResult != null ? lastResult.getUpdateTime() : null);
                state.setResultId(lastResult != null ? lastResult.getId() : null);
                state.setTranslationTime(lastResult != null ? lastResult.getUpdateTime() : null);

                states.add(state);
            });
        });

        // 保存数据
        // 每次插入100条
        for (int i = 0; i < states.size(); i += 100) {
            List<EntryState> part = states.subList(i, Math.min(i + 100, states.size()));
            entryStateService.saveBatch(part);
        }
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:entry:create:'+#request.moduleId")
    public void create(EntryCreateRequest request) {
        List<Branch> branches = branchMapper.findByNameIn(request.getModuleId(), request.getBranches());
        if(branches.size() != request.getBranches().size()) {
            throw new ResultException(I18nCode.ENTRY_CREATE_BRANCHES.code, MessageUtils.get(I18nCode.ENTRY_CREATE_BRANCHES.key));
        }
        branches.forEach(branch -> {
            int size = baseMapper.countByModuleIdAndBranchIdAndIdentifier(request.getModuleId(), branch.getId(), request.getKey());
            if(size > 0) {
                throw new ResultException(I18nCode.ENTRY_CREATE_KEY.code, MessageUtils.get(I18nCode.ENTRY_CREATE_KEY.key));
            }
            Entry entry = new Entry();
            entry.setModuleId(request.getModuleId());
            entry.setBranchId(branch.getId());
            entry.setIdentifier(request.getKey());
            entry.setValue(request.getValue());
            entry.setCreateUserId(request.getUserId());
            save(entry);

            activityService.entity(request.getModuleId(), ActivityEntryType.ENTRY.code, ActivityOperate.ADD.code, entry);
        });
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:entry:'+#request.id")
    @Caching(evict = {
            @CacheEvict(value = "i18n:entry:detail", key = "#request.id"),
            @CacheEvict(value = "i18n:entry", key = "#request.id")
    })
    public void edit(EntryEditRequest request) {
        Entry entry = getById(request.getId());

        entry.setValue(request.getValue());
        entry.setUpdateUserId(request.getUserId());
        updateById(entry);

        activityService.entity(entry.getModuleId(), ActivityEntryType.ENTRY.code, ActivityOperate.UPDATE.code, entry);
    }

    @Override
    @Cacheable(value = "i18n:entry:detail", key = "#id")
    public EntryWithStateResponse detail(Long id, String language) {
        Entry entry = getById(id);
        EntryWithStateResponse response = entryMapStruct.toEntryWithStateResponse(entry);
        EntryState state = entryStateMapper.findOneByEntryIdAndLanguage(id, language);
        response.setTranslated(state != null && state.getTranslated());
        response.setVerified(state != null && state.getVerified());
        if(state != null && response.getTranslated()) {
            EntryResult result = entryResultService.getById(state.getResultId());
            response.setTranslation(result);
        }
        return response;
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:entry:'+#id")
    @Caching(evict = {
            @CacheEvict(value = "i18n:entry:detail", key = "#id"),
            @CacheEvict(value = "i18n:entry", key = "#id")
    })
    public void remove(Long id, Long userId) {
        Entry entry = getById(id);

        entry.setDeleted(true);
        updateById(entry);

        activityService.entity(entry.getModuleId(), ActivityEntryType.ENTRY.code, ActivityOperate.DELETE.code, entry);
    }

    @Override
    public List<Entry> getByBranchId(Long branchId) {
        return baseMapper.findByBranchId(branchId);
    }

    @Override
    @Cacheable(value = "i18n:entry", key = "#id")
    public Entry findById(Long id) {
        return getById(id);
    }

    @Override
    public int countByBranchId(Long branchId) {
        return baseMapper.countByBranchIdAndDeleted(branchId, false);
    }

    @Override
    public List<Long> findIdByBranchId(Long branchId) {
        return baseMapper.findIdByBranchIdAndDeleted(branchId, false).stream().map(Entry::getId).toList();
    }
}
