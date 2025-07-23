package plus.xyc.server.i18n.entry.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.zkit.support.server.ai.api.service.VectorStoreApiService;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.common.enums.I18nCode;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.entity.dto.EntryState;
import plus.xyc.server.i18n.entry.entity.mapstruct.EntryMapStruct;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCreateRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryEditRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryListRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCountResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithResultResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithStateResponse;
import plus.xyc.server.i18n.entry.entity.response.UpdateEntriesResponse;
import plus.xyc.server.i18n.entry.mapper.EntryMapper;
import plus.xyc.server.i18n.entry.mapper.EntryResultMapper;
import plus.xyc.server.i18n.entry.mapper.EntryStateMapper;
import plus.xyc.server.i18n.entry.service.EntryResultService;
import plus.xyc.server.i18n.entry.service.EntryService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.entry.service.EntryStateService;
import plus.xyc.server.i18n.file.entity.dto.File;
import plus.xyc.server.i18n.file.entity.request.FileEntrySearchRequest;
import plus.xyc.server.i18n.file.mapper.FileMapper;
import plus.xyc.server.i18n.file.service.FileRevisionService;
import plus.xyc.server.i18n.file.service.FileService;
import plus.xyc.server.i18n.module.entity.dto.ModuleCount;
import plus.xyc.server.i18n.module.mapper.ModuleCountMapper;
import plus.xyc.server.i18n.module.mapper.ModuleTargetLanguageMapper;
import plus.xyc.server.i18n.open.entity.request.OpenEntryPullRequest;
import plus.xyc.server.i18n.open.entity.request.OpenEntryPushRequest;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private EntryStateMapper entryStateMapper;
    @Resource
    private ModuleCountMapper moduleCountMapper;
    @Resource
    private ModuleTargetLanguageMapper moduleTargetLanguageMapper;
    @Resource
    private EntryStateService entryStateService;
    @DubboReference
    private VectorStoreApiService vectorStoreApiService;
    @Resource
    private FileMapper fileMapper;
    @Lazy
    @Resource
    private FileRevisionService fileRevisionService;
    @Lazy
    @Resource
    private FileService fileService;

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
    @Transactional
    @DistributedLock("'i18n:entry:create:'+#request.moduleId")
    public void create(EntryCreateRequest request) {
        List<File> files = fileService.listByIds(request.getFiles());
        if(files.size() != request.getFiles().size()) {
            throw new ResultException(I18nCode.ENTRY_CREATE_FILES.code, MessageUtils.get(I18nCode.ENTRY_CREATE_FILES.key));
        }
        files.forEach(file -> {
            int size = baseMapper.countByModuleIdAndFileIdAndIdentifier(request.getModuleId(), file.getId(), request.getKey());
            if(size > 0) {
                throw new ResultException(I18nCode.ENTRY_CREATE_KEY.code, MessageUtils.get(I18nCode.ENTRY_CREATE_KEY.key));
            }
            Entry entry = new Entry();
            entry.setModuleId(request.getModuleId());
            entry.setFileId(file.getId());
            entry.setIdentifier(request.getKey());
            entry.setValue(request.getValue());
            entry.setCreateUserId(request.getUserId());
            save(entry);
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
        entry.setContext(request.getContext());
        entry.setUpdateUserId(request.getUserId());
        updateById(entry);
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
        log.info("push entry: {}", request);
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
    @Transactional
    public void deleteByIds(List<Long> ids) {
        if(ids == null || ids.isEmpty()) {
            return;
        }
        lambdaUpdate().in(Entry::getId, ids).set(Entry::getDeleted, true).update();
    }

    @Override
    public UpdateEntriesResponse updateEntries(Long moduleId, Long fileId, Long userId, List<EntryRequest> requestEntries) {
        UpdateEntriesResponse response = new UpdateEntriesResponse();
        if(requestEntries == null || requestEntries.isEmpty()) {
            response.setNewEntries(List.of());
            response.setUpdateEntries(List.of());
            response.setDeleteEntries(List.of());
            response.setOriginEntries(List.of());
            return response;
        }

        List<Entry> oldEntries = getByFileId(fileId);
        // clone 一份，不要被后续更新
        List<Entry> backup = JSON.parseArray(JSON.toJSONString(oldEntries), Entry.class);

        // 导入词条
        Date now = new Date();
        List<Entry> entries = new ArrayList<>();
        requestEntries.forEach(e -> {
            // 根据 identifier 查询 entries 中是否存在
            Entry existEntry = entries.stream().filter(entry -> entry.getIdentifier().equals(e.getIdentifier())).findFirst().orElse(null);
            if(existEntry != null) {
                return;
            }
            Entry entry = new Entry();
            entry.setModuleId(moduleId);
            entry.setIdentifier(e.getIdentifier());
            entry.setValue(e.getValue());
            entry.setContext(e.getContext());
            entry.setCreateTime(now);
            entry.setUpdateTime(now);
            entry.setFileId(fileId);
            entry.setCreateUserId(userId);
            entry.setUpdateUserId(userId);
            entry.setUpdateTime(now);
            entries.add(entry);
        });

        // 词条准备完毕，开始匹配新增、更新、删除
        List<Entry> newEntries = new ArrayList<>();
        List<Entry> updateEntries = new ArrayList<>();
        List<Entry> deleteEntries = new ArrayList<>();
        // 以 Identifier 的唯一性为依据，如果不在 oldEntries 中，则新增
        entries.forEach(entry -> {
            if (oldEntries.stream().noneMatch(e -> e.getIdentifier().equals(entry.getIdentifier()))) {
                newEntries.add(entry);
            }
        });
        // 以 Identifier 的唯一性为依据，如果 entries 中不存在，oldEntries 中存在，则删除
        oldEntries.forEach(oldEntry -> {
            if (entries.stream().noneMatch(e -> e.getIdentifier().equals(oldEntry.getIdentifier()))) {
                deleteEntries.add(oldEntry);
            }
        });
        // 以 Identifier、value、context 为依据，如果 Identifier 一致，value 或 context 不一致，则更新
        // context 可能为空
        oldEntries.forEach(oldEntry -> {
            if (entries.stream().anyMatch(e -> e.getIdentifier().equals(oldEntry.getIdentifier())
                    && !e.getValue().equals(oldEntry.getValue()) && (e.getContext() == null && oldEntry.getContext() == null || e.getContext().equals(oldEntry.getContext())))) {
                updateEntries.add(oldEntry);
            }
        });

        // 批量插入
        saveBatch(newEntries);
        // 批量更新
        updateEntries.forEach(entry -> {
            EntryRequest requestEntry = requestEntries.stream()
                    .filter(e -> e.getIdentifier().equals(entry.getIdentifier())).findFirst().orElse(null);
            entry.setValue(requestEntry.getValue());
            entry.setContext(requestEntry.getContext());
            entry.setUpdateTime(now);
            entry.setUpdateUserId(userId);
        });
        updateBatchById(updateEntries);
        // 批量删除
        deleteByIds(deleteEntries.stream().map(Entry::getId).toList());

        response.setNewEntries(newEntries);
        response.setUpdateEntries(updateEntries);
        response.setDeleteEntries(deleteEntries);
        response.setOriginEntries(backup);
        return response;
    }

    @Override
    public List<Entry> getByIds(List<Long> ids) {
        if(ids == null || ids.isEmpty()) {
            return List.of();
        }
        return lambdaQuery().in(Entry::getId, ids).list();
    }

    @Override
    public PageResult<EntryResponse> list(PageRequest pr, FileEntrySearchRequest request) {
        try(Page<Entry> page = pr.start()) {
            request.setKeyword(pr.getKeyword());
            if(request.getFileId() != null) {
                request.setFileIdList(Arrays.stream(request.getFileId().split(",")).map(Long::parseLong).toList());
            }
            baseMapper.list(request);
            List<Entry> entries = page.getResult();
            List<Long> fileIds = entries.stream().distinct().map(Entry::getFileId).toList();
            List<File> files = List.of();
            if(!fileIds.isEmpty()) {
                files = fileService.listByIds(fileIds);
            }
            Map<Long, File> fileMap = files.stream().collect(Collectors.toMap(File::getId, Function.identity()));
            List<EntryResponse> responses = entries.stream().map(entry -> {
                EntryResponse response = entryMapStruct.toEntryResponse(entry);
                response.setFile(fileMap.get(entry.getFileId()));
                return response;
            }).toList();
            return PageResult.of(page.getTotal(), responses);
        }
    }
}
