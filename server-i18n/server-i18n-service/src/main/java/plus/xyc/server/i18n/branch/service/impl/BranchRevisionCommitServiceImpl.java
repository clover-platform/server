package plus.xyc.server.i18n.branch.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.aop.framework.AopContext;
import plus.xyc.server.i18n.branch.entity.dto.BranchRevisionCommit;
import plus.xyc.server.i18n.branch.entity.enums.CommitAction;
import plus.xyc.server.i18n.branch.mapper.BranchRevisionCommitMapper;
import plus.xyc.server.i18n.branch.service.BranchRevisionCommitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}
