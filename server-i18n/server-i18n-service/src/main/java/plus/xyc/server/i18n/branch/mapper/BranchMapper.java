package plus.xyc.server.i18n.branch.mapper;

import org.apache.ibatis.annotations.Param;
import plus.xyc.server.i18n.branch.entity.dto.Branch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.i18n.branch.entity.request.AllBranchRequest;

import java.util.List;

/**
 * <p>
 * 分支 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface BranchMapper extends BaseMapper<Branch> {

    List<Branch> all(@Param("request") AllBranchRequest request);

}
