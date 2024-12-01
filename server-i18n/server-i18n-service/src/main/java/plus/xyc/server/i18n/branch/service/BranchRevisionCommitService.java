package plus.xyc.server.i18n.branch.service;

import plus.xyc.server.i18n.branch.entity.dto.BranchRevisionCommit;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.entry.entity.dto.Entry;

import java.util.List;

/**
 * <p>
 * 变更详情 服务类
 * </p>
 *
 * @author generator
 * @since 2024-11-29
 */
public interface BranchRevisionCommitService extends IService<BranchRevisionCommit> {

    void add(Long revisionId, List<Long> entryIds);
    void delete(Long revisionId, List<Long> entries);
    void update(Long revisionId, List<Entry> entries, List<Entry> origin);

}
