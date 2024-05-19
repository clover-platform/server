package plus.xyc.server.i18n.branch.service;

import plus.xyc.server.i18n.branch.entity.dto.Branch;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.branch.entity.request.AllBranchRequest;
import plus.xyc.server.i18n.branch.entity.response.BranchResponse;

import java.util.List;

/**
 * <p>
 * 分支 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface BranchService extends IService<Branch> {

    void createDefault(Long moduleId);
    List<BranchResponse> all(AllBranchRequest request);

}
