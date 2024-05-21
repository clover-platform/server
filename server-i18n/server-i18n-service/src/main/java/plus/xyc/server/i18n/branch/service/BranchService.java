package plus.xyc.server.i18n.branch.service;

import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.branch.entity.dto.Branch;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.branch.entity.request.BranchAllRequest;
import plus.xyc.server.i18n.branch.entity.request.BranchCreateRequest;
import plus.xyc.server.i18n.branch.entity.request.BranchListRequest;
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
    List<BranchResponse> all(BranchAllRequest request);
    PageResult<Branch> list(PageQueryRequest page, BranchListRequest request);
    void create(BranchCreateRequest request);
    Branch getDefault(Long moduleId);

}
