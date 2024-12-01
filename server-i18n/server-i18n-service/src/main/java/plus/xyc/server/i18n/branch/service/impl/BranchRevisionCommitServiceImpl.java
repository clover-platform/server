package plus.xyc.server.i18n.branch.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.aop.framework.AopContext;
import plus.xyc.server.i18n.branch.entity.dto.BranchRevisionCommit;
import plus.xyc.server.i18n.branch.entity.enums.CommitAction;
import plus.xyc.server.i18n.branch.mapper.BranchRevisionCommitMapper;
import plus.xyc.server.i18n.branch.service.BranchRevisionCommitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.entry.entity.dto.Entry;

import java.util.List;

/**
 * <p>
 * 变更详情 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-11-29
 */
@Service
public class BranchRevisionCommitServiceImpl extends ServiceImpl<BranchRevisionCommitMapper, BranchRevisionCommit> implements BranchRevisionCommitService {

    @Override
    @Transactional
    public void add(Long revisionId, List<Long> entryIds) {
        // 批量插入
        List<BranchRevisionCommit> commits = entryIds.stream()
                .map(entryId -> new BranchRevisionCommit()
                        .setRevisionId(revisionId).setEntryId(entryId)
                        .setAction(CommitAction.ADD.value())
                ).toList();
        BranchRevisionCommitService self = (BranchRevisionCommitService) AopContext.currentProxy();
        self.saveBatch(commits);
    }

    @Override
    @Transactional
    public void delete(Long revisionId, List<Long> entries) {
        // 批量插入
        List<BranchRevisionCommit> commits = entries.stream()
                .map(entryId -> new BranchRevisionCommit()
                        .setRevisionId(revisionId).setEntryId(entryId)
                        .setAction(CommitAction.DELETE.value())
                ).toList();
        BranchRevisionCommitService self = (BranchRevisionCommitService) AopContext.currentProxy();
        self.saveBatch(commits);
    }

    @Override
    public void update(Long revisionId, List<Entry> entries, List<Entry> origins) {
        List<BranchRevisionCommit> commits = entries.stream()
                .map(entry -> {
                    BranchRevisionCommit commit = new BranchRevisionCommit()
                            .setRevisionId(revisionId).setEntryId(entry.getId());
                    Entry origin = origins.stream().filter(o -> o.getId().equals(entry.getId())).findFirst().orElse(null);
                    commit.setAction(CommitAction.UPDATE.value())
                            .setOrigin(origin)
                            .setCurrent(entry);
                    return commit;
                }).toList();
        BranchRevisionCommitService self = (BranchRevisionCommitService) AopContext.currentProxy();
        self.saveBatch(commits);
    }
}
