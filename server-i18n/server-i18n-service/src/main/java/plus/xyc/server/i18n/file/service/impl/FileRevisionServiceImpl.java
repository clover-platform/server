package plus.xyc.server.i18n.file.service.impl;

import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.entity.dto.EntryState;
import plus.xyc.server.i18n.entry.entity.request.EntryRequest;
import plus.xyc.server.i18n.entry.entity.response.UpdateEntriesResponse;
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
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
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
@Slf4j
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
    public void add(Long moduleId, Long fileId, Long userId, String url, List<EntryRequest> requestEntries) {
        if (requestEntries.isEmpty()) {
            return;
        }

        Date now = new Date();
        List<ModuleLanguageResponse> languages = moduleTargetLanguageService.languages(moduleId);
        UpdateEntriesResponse response = entryService.updateEntries(moduleId, fileId, userId, requestEntries);
        List<Entry> newEntries = response.getNewEntries();
        List<Entry> updateEntries = response.getUpdateEntries();
        List<Entry> deleteEntries = response.getDeleteEntries();
        List<Entry> originEntries = response.getOriginEntries();

        // 导入结果
        List<EntryResult> results = new ArrayList<>();
        requestEntries.forEach(requestEntry -> {
            Entry entry = originEntries.stream().filter(e -> e.getIdentifier().equals(
                    requestEntry.getIdentifier())).findFirst().orElse(null);
            if(entry == null) {
                return;
            }
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
        List<Long> resultEntryIds = results.stream().map(EntryResult::getEntryId).toList();
        List<EntryResult> oldResults = entryResultService.getResults(resultEntryIds);
        // 如果 results 中存在与 oldResults 中相同的，则不需要保存，判断相同的依据是 entryId & language & content 相同
        results.removeIf(result -> oldResults.stream().anyMatch(r -> r.getEntryId().equals(result.getEntryId()) && r.getLanguage().equals(result.getLanguage()) && r.getContent().equals(result.getContent())));
        entryResultService.saveBatch(results);

        List<Long> needUpdateStateEntryIds = new ArrayList<>();
        results.forEach(result -> {
            needUpdateStateEntryIds.add(result.getEntryId());
        });
        newEntries.forEach(entry -> {
            needUpdateStateEntryIds.add(entry.getId());
        });
        // 去重
        List<Long> distinctEntryIds = needUpdateStateEntryIds.stream().distinct().toList();
        List<Entry> needUpdateStateEntries = entryService.getByIds(distinctEntryIds);

        // 更新状态
        List<EntryState> oldStates = entryStateService.findListByEntryIds(distinctEntryIds);
        List<EntryState> newStates = new ArrayList<>();
        List<EntryState> updateStates = new ArrayList<>();
        // 补充初始化 states
        languages.forEach(language -> {
            needUpdateStateEntries.forEach(entry -> {
                // 根据 entry.id & language.code 在 oldStates 中找到对应的 state
                EntryState state = oldStates.stream().filter(s -> s.getEntryId().equals(entry.getId()) && s.getLanguage().equals(language.getCode())).findFirst().orElse(null);
                // 根据 entry.id & language.code 在 results 中找到对应的 resultId
                EntryResult result = results.stream()
                        .filter(r -> r.getEntryId().equals(entry.getId()) && r.getLanguage().equals(language.getCode()))
                        .findFirst().orElse(null);
                log.info("state entryId: {}, state: {}, result: {}", entry.getId(), state,  result);
                if(state == null) {
                    state = new EntryState();
                    state.setEntryId(entry.getId());
                    state.setLanguage(language.getCode());
                    if(result != null) {
                        state.setResultId(result.getId());
                        state.setTranslated(true);
                        state.setTranslationTime(now);
                    }else{
                        state.setTranslated(false);
                    }
                    newStates.add(state);
                }else{
                    if(result != null) {
                        state.setResultId(result.getId());
                        state.setTranslated(true);
                        state.setTranslationTime(now);
                        updateStates.add(state);
                    }
                }
            });
        });
        entryStateService.saveBatch(newStates);
        entryStateService.updateBatchById(updateStates);

        if(newEntries.size() == 0 && updateEntries.size() == 0 && deleteEntries.size() == 0) {
            return;
        }
        // 更新 revision
        FileRevision revision = findCurrentByFileId(fileId);
        String message = MessageUtils.get("branch.revision.init.message");
        int version = 1;
        if(revision != null) {
            lambdaUpdate().eq(FileRevision::getId, revision.getId()).set(FileRevision::getCurrent, false).update();
            version = revision.getVersion() + 1;
            message = DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss");
        }
        revision = new FileRevision();
        revision.setFileId(fileId);
        revision.setCreateTime(now);
        revision.setCreateUser(userId);
        revision.setMessage(message);
        revision.setFileUrl(url);
        revision.setCurrent(true);
        revision.setVersion(version);
        revision.setAddedSize(newEntries.size());
        revision.setUpdatedSize(updateEntries.size());
        revision.setDeletedSize(deleteEntries.size());
        save(revision);

        // 插入 commit 记录
        fileRevisionCommitService.add(revision.getId(), newEntries.stream().map(Entry::getId).toList());
        fileRevisionCommitService.delete(revision.getId(), deleteEntries.stream().map(Entry::getId).toList());
        // originEntries 需要过滤，只需要 与 updateEntries 的 id 相同的
        fileRevisionCommitService.update(revision.getId(), updateEntries, originEntries.stream()
                .filter(entry -> updateEntries.stream().anyMatch(e -> e.getId().equals(entry.getId()))).toList());

        // 更新词条数量
        moduleCountService.updateCount(moduleId, fileId);
    }

    @Override
    public List<FileRevision> findListByFileIds(List<Long> fileIds) {
        if(fileIds == null || fileIds.isEmpty()) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(new LambdaQueryWrapper<FileRevision>().in(FileRevision::getFileId, fileIds).eq(FileRevision::getCurrent, true));
    }

    @Override
    public FileRevision findCurrentByFileId(Long fileId) {
        return lambdaQuery().eq(FileRevision::getFileId, fileId).eq(FileRevision::getCurrent, true).one();
    }

}
