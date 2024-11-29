package plus.xyc.server.i18n.branch.service.impl;

import jakarta.annotation.Resource;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.branch.entity.dto.BranchRevision;
import plus.xyc.server.i18n.branch.mapper.BranchRevisionMapper;
import plus.xyc.server.i18n.branch.service.BranchRevisionCommitService;
import plus.xyc.server.i18n.branch.service.BranchRevisionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.entry.service.EntryService;

import java.util.List;

/**
 * <p>
 * 变更 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-11-29
 */
@Service
public class BranchRevisionServiceImpl extends ServiceImpl<BranchRevisionMapper, BranchRevision> implements BranchRevisionService {

    @Resource
    private EntryService entryService;
    @Resource
    private BranchRevisionCommitService branchRevisionCommitService;

    @Override
    @DistributedLock("'i18n:branch:revision:create:'+#branchId")
    public void init(Long branchId, Long userId) {
        String message = MessageUtils.get("branch.revision.init.message");
        List<Long> ids = entryService.findIdByBranchId(branchId);
        BranchRevision revision = new BranchRevision()
                .setBranchId(branchId)
                .setMessage(message)
                .setCurrent(true)
                .setCreateUser(userId)
                .setAddedSize(ids.size())
                .setUpdatedSize(0)
                .setDeletedSize(0);
        save(revision);
        branchRevisionCommitService.add(revision.getId(), ids);
    }
}
