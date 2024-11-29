package plus.xyc.server.i18n.branch.service;

import plus.xyc.server.i18n.branch.entity.dto.BranchRevision;
import com.baomidou.mybatisplus.extension.service.IService;

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

}
