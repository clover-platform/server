package plus.xyc.server.i18n.branch.service;

import plus.xyc.server.i18n.branch.entity.dto.BranchRevision;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.branch.entity.request.NewRevisionRequest;
import plus.xyc.server.i18n.entry.entity.dto.Entry;

import java.util.List;

/**
 * <p>
 * 变更 服务类
 * </p>
 *
 * @author generator
 * @since 2024-11-29
 */
public interface BranchRevisionService extends IService<BranchRevision> {

    void init(Long branchId, Long userId);
    Long newRevision(NewRevisionRequest request);
    void resetCurrent(Long branchId, Long revisionId);
    void add(Long revisionId, List<Long> entries);
    void delete(Long revisionId, List<Long> entries);
    void update(Long revisionId, List<Entry> entries, List<Entry> origin);

}
