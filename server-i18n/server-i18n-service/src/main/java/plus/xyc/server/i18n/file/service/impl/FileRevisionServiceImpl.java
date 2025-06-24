package plus.xyc.server.i18n.file.service.impl;

import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.entity.dto.EntryState;
import plus.xyc.server.i18n.entry.entity.request.EntryRequest;
import plus.xyc.server.i18n.entry.service.EntryResultService;
import plus.xyc.server.i18n.entry.service.EntryService;
import plus.xyc.server.i18n.entry.service.EntryStateService;
import plus.xyc.server.i18n.file.entity.dto.FileRevision;
import plus.xyc.server.i18n.file.mapper.FileRevisionMapper;
import plus.xyc.server.i18n.file.service.FileRevisionCommitService;
import plus.xyc.server.i18n.file.service.FileRevisionService;
import plus.xyc.server.i18n.module.entity.response.ModuleLanguageResponse;
import plus.xyc.server.i18n.module.service.ModuleCountService;
import plus.xyc.server.i18n.module.service.ModuleTargetLanguageService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.zkit.support.starter.boot.utils.MessageUtils;

/**
 * <p>
 * 变更 服务实现类
 * </p>
 *
 * @author generator
 * @since 2025-06-17
 */
@Service
public class FileRevisionServiceImpl extends ServiceImpl<FileRevisionMapper, FileRevision> implements FileRevisionService {

    @Resource
    private EntryService entryService;
    @Resource
    private FileRevisionCommitService fileRevisionCommitService;
    @Resource
    private EntryResultService entryResultService;
    @Resource
    private EntryStateService entryStateService;
    @Resource
    private ModuleTargetLanguageService moduleTargetLanguageService;
    @Resource
    private ModuleCountService moduleCountService;

    @Override
    @Transactional
    public void init(Long moduleId, Long fileId, Long userId, String url, List<EntryRequest> requestEntries) {
        if (requestEntries.isEmpty()) {
            return;
        }

        List<ModuleLanguageResponse> languages = moduleTargetLanguageService.languages(moduleId);

        // 导入词条
        Date now = new Date();
        List<Entry> entries = new ArrayList<>();
        requestEntries.forEach(e -> {
            Entry entry = new Entry();
            entry.setModuleId(moduleId);
            entry.setIdentifier(e.getIdentifier());
            entry.setValue(e.getValue());
            entry.setCreateTime(now);
            entry.setUpdateTime(now);
            entry.setFileId(fileId);
            entry.setCreateUserId(userId);
            entry.setUpdateUserId(userId);
            entries.add(entry);
        });

        entryService.saveBatch(entries);

        List<EntryResult> results = new ArrayList<>();

        final AtomicInteger index = new AtomicInteger(0);
        entries.forEach(entry -> {
            EntryRequest requestEntry = requestEntries.get(index.getAndIncrement());
            List<EntryRequest.Result> requestResults = requestEntry.getResults();
            if(requestResults == null || requestResults.isEmpty()) {
                return;
            }
            requestResults.forEach(result -> {
                EntryResult entryResult = new EntryResult();
                entryResult.setEntryId(entry.getId());
                entryResult.setLanguage(result.getLanguage());
                entryResult.setContent(result.getContent());
                entryResult.setTranslatorId(userId);
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

        // 补充初始化 states
        languages.forEach(language -> {
            entries.forEach(entry -> {
                // 判断 entry.id & language.code 是否 在 states 中
                if(states.stream().anyMatch(state -> state.getEntryId().equals(entry.getId()) && state.getLanguage().equals(language.getCode()))) {
                    return;
                }
                EntryState state = new EntryState();
                state.setEntryId(entry.getId());
                state.setLanguage(language.getCode());
                state.setResultId(null);
                state.setTranslated(false);
                states.add(state);
            });
        });
        entryStateService.saveBatch(states);

        String message = MessageUtils.get("branch.revision.init.message");
        FileRevision revision = new FileRevision();
        revision.setFileId(fileId);
        revision.setCreateTime(now);
        revision.setCreateUser(userId);
        revision.setMessage(message);
        revision.setFileUrl(url);
        revision.setCurrent(true);
        revision.setAddedSize(entries.size());
        revision.setUpdatedSize(0);
        revision.setDeletedSize(0);
        save(revision);

        // 插入 commit 记录
        fileRevisionCommitService.add(revision.getId(), entries.stream().map(Entry::getId).toList());

        moduleCountService.updateCount(moduleId, fileId);
    }

    @Override
    public List<FileRevision> findListByFileIds(List<Long> fileIds) {
        if(fileIds == null || fileIds.isEmpty()) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(new LambdaQueryWrapper<FileRevision>().in(FileRevision::getFileId, fileIds).eq(FileRevision::getCurrent, true));
    }

}
