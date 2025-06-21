package plus.xyc.server.i18n.entry.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.zkit.support.server.ai.api.entity.Document;
import org.zkit.support.server.ai.api.service.AIAPIService;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.activity.entity.enums.ActivityEntryType;
import plus.xyc.server.i18n.activity.entity.enums.ActivityOperate;
import plus.xyc.server.i18n.activity.service.ActivityService;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.entity.dto.EntryState;
import plus.xyc.server.i18n.entry.entity.mapstruct.EntryMapStruct;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCreateRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryEditRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryImportRequest;
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
import plus.xyc.server.i18n.file.entity.dto.File;
import plus.xyc.server.i18n.file.mapper.FileMapper;
import plus.xyc.server.i18n.module.entity.dto.ModuleCount;
import plus.xyc.server.i18n.module.entity.dto.ModuleTargetLanguage;
import plus.xyc.server.i18n.module.mapper.ModuleCountMapper;
import plus.xyc.server.i18n.module.mapper.ModuleTargetLanguageMapper;
import plus.xyc.server.i18n.open.entity.request.OpenEntryPullRequest;
import plus.xyc.server.i18n.open.entity.request.OpenEntryPushRequest;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    private EntryStateMapper entryStateMapper;
    @Resource
    private ModuleCountMapper moduleCountMapper;
    @Resource
    private ModuleTargetLanguageMapper moduleTargetLanguageMapper;
    @Resource
    private EntryStateService entryStateService;
    @Resource
    private AIAPIService aiapiService;
    @Resource
    private FileMapper fileMapper;

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
    public PageResult<EntryWithStateResponse> all(EntryListRequest request) {
        int count = baseMapper.countByModuleIdAndFileId(request.getModuleId(), request.getFileId());
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setSize(count);
        return this.query(pageRequest, request);
    }

    @Override
    public void sync(EntryListRequest request) {
        PageResult<EntryWithStateResponse> all = this.all(request);
        List<EntryWithStateResponse> entries = all.getData();
        List<Document> documents = entries.stream().filter(EntryWithStateResponse::getTranslated).map(entry -> {
            Document document = new Document();
            document.setId(entry.getId().toString() + "-" + request.getLanguage());
            document.setPage_content("source:["+entry.getValue() + "], result:[" + entry.getTranslation().getContent()+"]");
            JSONObject meta = new JSONObject();
            meta.put("source", "i18n");
            meta.put("language", request.getLanguage());
            document.setMetadata(meta);
            return document;
        }).toList();
        Result<?> r = aiapiService.addDocuments(documents);
        if(!r.isSuccess()) {
            throw ResultException.internal();
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
    public List<EntryWithResultResponse> getEntryByFileIdWithResult(Long fileId) {
        List<Entry> entries = baseMapper.findByFileId(fileId);
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
        List<EntryWithResultResponse> entries = getEntryByFileIdWithResult(sourceId);
        cloneEntries(entries, targetId);
    }

    @Override
    public void cloneEntries(List<EntryWithResultResponse> sources, Long targetId) {
        File target = fileMapper.selectById(targetId);
        List<ModuleTargetLanguage> languages = moduleTargetLanguageMapper.findByModuleId(target.getModuleId());
        sources.forEach(entry -> {
            entry.setId(null);
            entry.setFileId(targetId);
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
        // List<File> branches = fileMapper.findByNameIn(request.getModuleId(), request.getBranches());
        // if(branches.size() != request.getBranches().size()) {
        //     throw new ResultException(I18nCode.ENTRY_CREATE_BRANCHES.code, MessageUtils.get(I18nCode.ENTRY_CREATE_BRANCHES.key));
        // }
        // branches.forEach(branch -> {
        //     int size = baseMapper.countByModuleIdAndBranchIdAndIdentifier(request.getModuleId(), branch.getId(), request.getKey());
        //     if(size > 0) {
        //         throw new ResultException(I18nCode.ENTRY_CREATE_KEY.code, MessageUtils.get(I18nCode.ENTRY_CREATE_KEY.key));
        //     }
        //     Entry entry = new Entry();
        //     entry.setModuleId(request.getModuleId());
        //     entry.setFileId(branch.getId());
        //     entry.setIdentifier(request.getKey());
        //     entry.setValue(request.getValue());
        //     entry.setCreateUserId(request.getUserId());
        //     save(entry);

        //     activityService.entity(request.getModuleId(), ActivityEntryType.ENTRY.code, ActivityOperate.ADD.code, entry);
        // });
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
    public List<Entry> getByFileId(Long fileId) {
        return baseMapper.findByFileId(fileId);
    }

    @Override
    @Cacheable(value = "i18n:entry", key = "#id")
    public Entry findById(Long id) {
        return getById(id);
    }

    @Override
    public int countByFileId(Long fileId) {
        return baseMapper.countByFileIdAndDeleted(fileId, false);
    }

    @Override
    public List<Long> findIdByFileId(Long fileId) {
        return baseMapper.findIdByFileIdAndDeleted(fileId, false).stream().map(Entry::getId).toList();
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:entry:push:'+#request.moduleId")
    public void push(OpenEntryPushRequest request) {
        // EntryService self = AopUtils.current(EntryService.class);
        // File branch = fileMapper.selectById(request.getBranchId());

        // 当前所有的词条
        List<Entry> entries = baseMapper.findByFileId(request.getFileId());

        log.info("entries: {}", entries);

        List<Entry> addList = new ArrayList<>();
        List<Entry> updateList = new ArrayList<>();
        List<Entry> deleteList = new ArrayList<>();

        // 新增的 和 需要更新的
        request.getContent().forEach((key, v) -> {
            String value = (String) v;
            Entry entry = entries.stream().filter(item -> item.getIdentifier().equals(key)).findFirst().orElse(null);
            if(entry == null) {
                Entry newEntry = new Entry();
                newEntry.setModuleId(request.getModuleId());
                newEntry.setFileId(request.getFileId());
                newEntry.setIdentifier(key);
                newEntry.setValue(value);
                newEntry.setCreateUserId(request.getUserId());
                addList.add(newEntry);
            } else if(!v.equals(entry.getValue())) {
                entry.setValue(value);
                entry.setUpdateUserId(request.getUserId());
                updateList.add(entry);
            }
        });

        // 需要删除的
        entries.forEach(entry -> {
            if(!request.getContent().containsKey(entry.getIdentifier())) {
                deleteList.add(entry);
            }
        });

        // 如果都是空的，直接返回
        if(addList.isEmpty() && updateList.isEmpty() && deleteList.isEmpty()) {
            return;
        }

        log.info("push entry addList: {}, updateList: {}, deleteList: {}", addList.size(), updateList.size(), deleteList.size());

        // TODO 
        // 新版本
        // NewRevisionRequest newRevisionRequest = new NewRevisionRequest();
        // newRevisionRequest.setBranchId(request.getBranchId());
        // newRevisionRequest.setUserId(request.getUserId());
        // String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // newRevisionRequest.setMessage(MessageUtils.get("branch.revision.push.message", branch.getName(), time));
        // Long revisionId = branchRevisionService.newRevision(newRevisionRequest);

        // // 新增
        // if(!addList.isEmpty()) {
        //     self.saveBatch(addList);
        //     List<Long> entryIds = addList.stream().map(Entry::getId).toList();
        //     branchRevisionService.add(revisionId, entryIds);
        // }

        // // 更新
        // if(!updateList.isEmpty()) {
        //     self.updateBatchById(updateList);
        //     List<Entry> updateOriginEntries = entries.stream().filter(item -> updateList.stream().map(Entry::getId).toList().contains(item.getId())).toList();
        //     branchRevisionService.update(revisionId, updateList, updateOriginEntries);
        // }

        // // 删除
        // if(!deleteList.isEmpty()) {
        //     List<Long> entryIds = deleteList.stream().map(Entry::getId).toList();
        //     lambdaUpdate().set(Entry::getDeleted, true).in(Entry::getId, entryIds).update();
        //     branchRevisionService.delete(revisionId, entryIds);
        // }
    }

    @Override
    public JSONObject pull(OpenEntryPullRequest request) {
        int count = this.countByFileId(request.getFileId());
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setSize(count);
        EntryListRequest queryRequest = new EntryListRequest();
        queryRequest.setModuleId(request.getModuleId());
        queryRequest.setFileId(request.getFileId());
        queryRequest.setLanguage(request.getLanguage());
        PageResult<EntryWithStateResponse> result = this.query(pageRequest, queryRequest);
        JSONObject response = new JSONObject();
        result.getData().forEach(entry -> {
            if(entry.getTranslated()) {
                response.put(entry.getIdentifier(), entry.getTranslation().getContent());
            }else{
                response.put(entry.getIdentifier(), entry.getValue());
            }
        });
        return response;
    }

    @Override
    public void importEntries(EntryImportRequest request) {
        log.info("import entries: {}", JSON.toJSONString(request));
        Date now = new Date();
        List<Entry> entries = request.getEntries().stream().map(entry -> {
            Entry e = new Entry();
            e.setModuleId(request.getModuleId());
            e.setFileId(request.getFileId());
            e.setIdentifier(entry.getIdentifier());
            e.setValue(entry.getValue());
            e.setContext(entry.getContext());
            e.setCreateUserId(request.getUserId());
            e.setUpdateUserId(request.getUserId());
            e.setCreateTime(now);
            e.setUpdateTime(now);
            return e;
        }).toList();
        saveBatch(entries);

        List<EntryResult> results = new ArrayList<>();
        
        final AtomicInteger index = new AtomicInteger(0);
        entries.forEach(entry -> {
            EntryImportRequest.Entry requestEntry = request.getEntries().get(index.getAndIncrement());
            List<EntryImportRequest.Result> requestResults = requestEntry.getResults();
            requestResults.forEach(result -> {
                EntryResult entryResult = new EntryResult();
                entryResult.setEntryId(entry.getId());
                entryResult.setLanguage(result.getLanguage());
                entryResult.setContent(result.getContent());
                entryResult.setTranslatorId(request.getUserId());
                results.add(entryResult);
            });
        });
        entryResultService.saveBatch(results);

        List<EntryState> states = new ArrayList<>();
        results.forEach(result -> {
            EntryState state = new EntryState();
            state.setEntryId(result.getEntryId());
            state.setLanguage(result.getLanguage());
            state.setResultId(result.getId());
            state.setTranslated(true);
            state.setVerified(false);
            state.setTranslationTime(now);
            states.add(state);
        });
        entryStateService.saveBatch(states);
    }
}
