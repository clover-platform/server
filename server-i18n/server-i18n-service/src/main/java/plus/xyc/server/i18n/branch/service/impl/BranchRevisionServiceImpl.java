package plus.xyc.server.i18n.branch.service.impl;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.zkit.support.starter.boot.utils.AopUtils;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.branch.entity.dto.BranchRevision;
import plus.xyc.server.i18n.branch.entity.mapstruct.BranchRevisionMapStruct;
import plus.xyc.server.i18n.branch.entity.request.NewRevisionRequest;
import plus.xyc.server.i18n.branch.mapper.BranchRevisionMapper;
import plus.xyc.server.i18n.branch.service.BranchRevisionCommitService;
import plus.xyc.server.i18n.branch.service.BranchRevisionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
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
@Slf4j
public class BranchRevisionServiceImpl extends ServiceImpl<BranchRevisionMapper, BranchRevision> implements BranchRevisionService {

    @Resource
    private EntryService entryService;
    @Resource
    private BranchRevisionCommitService branchRevisionCommitService;
    @Resource
    private BranchRevisionMapStruct branchRevisionMapStruct;

    @Override
    @Transactional
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

    @Override
    @Transactional
    @DistributedLock("'i18n:branch:revision:create:'+#request.branchId")
    public Long newRevision(NewRevisionRequest request) {
        log.info("newRevision: {}", request);
        BranchRevision revision = branchRevisionMapStruct.toBranchRevision(request);
        revision.setAddedSize(0)
                .setUpdatedSize(0)
                .setDeletedSize(0);
        save(revision);
        BranchRevisionService self = AopUtils.current(BranchRevisionService.class);
        self.resetCurrent(request.getBranchId(), revision.getId());
        return revision.getId();
    }

    @Override
    public void resetCurrent(Long branchId, Long revisionId) {
        lambdaUpdate()
                .eq(BranchRevision::getBranchId, branchId)
                .set(BranchRevision::getCurrent, false)
                .update();
        lambdaUpdate()
                .eq(BranchRevision::getId, revisionId)
                .set(BranchRevision::getCurrent, true)
                .update();
    }

    @Override
    public void add(Long revisionId, List<Long> entries) {
        branchRevisionCommitService.add(revisionId, entries);
    }

    @Override
    public void delete(Long revisionId, List<Long> entries) {
        branchRevisionCommitService.delete(revisionId, entries);
    }

    @Override
    public void update(Long revisionId, List<Entry> entries, List<Entry> origin) {
        branchRevisionCommitService.update(revisionId, entries, origin);
    }
}
