package plus.xyc.server.i18n.file.service.impl;

import plus.xyc.server.i18n.file.entity.dto.FileRevisionCommit;
import plus.xyc.server.i18n.file.mapper.FileRevisionCommitMapper;
import plus.xyc.server.i18n.file.service.FileRevisionCommitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.file.entity.enums.CommitAction;
import plus.xyc.server.i18n.entry.entity.dto.Entry;

/**
 * <p>
 * 变更详情 服务实现类
 * </p>
 *
 * @author generator
 * @since 2025-06-17
 */
@Service
public class FileRevisionCommitServiceImpl extends ServiceImpl<FileRevisionCommitMapper, FileRevisionCommit> implements FileRevisionCommitService {

    @Override
    @Transactional
    public void add(Long revisionId, List<Long> entryIds) {
        // 批量插入
        List<FileRevisionCommit> commits = entryIds.stream()
                .map(entryId -> new FileRevisionCommit()
                        .setRevisionId(revisionId).setEntryId(entryId)
                        .setAction(CommitAction.ADD.value()))
                .toList();
        FileRevisionCommitService self = (FileRevisionCommitService) AopContext.currentProxy();
        self.saveBatch(commits);
    }

    @Override
    @Transactional
    public void delete(Long revisionId, List<Long> entries) {
        // 批量插入
        List<FileRevisionCommit> commits = entries.stream()
                .map(entryId -> new FileRevisionCommit()
                        .setRevisionId(revisionId).setEntryId(entryId)
                        .setAction(CommitAction.DELETE.value()))
                .toList();
        FileRevisionCommitService self = (FileRevisionCommitService) AopContext.currentProxy();
        self.saveBatch(commits);
    }

    @Override
    @Transactional
    public void update(Long revisionId, List<Entry> entries, List<Entry> origin) {
        List<FileRevisionCommit> commits = entries.stream()
                .map(entry -> {
                    FileRevisionCommit commit = new FileRevisionCommit()
                            .setRevisionId(revisionId).setEntryId(entry.getId());
                    Entry originEntry = origin.stream().filter(o -> o.getId().equals(entry.getId())).findFirst()
                            .orElse(null);
                    commit.setAction(CommitAction.UPDATE.value())
                            .setOrigin(originEntry)
                            .setCurrent(entry);
                    return commit;
                }).toList();
        FileRevisionCommitService self = (FileRevisionCommitService) AopContext.currentProxy();
        self.saveBatch(commits);
    }

}
