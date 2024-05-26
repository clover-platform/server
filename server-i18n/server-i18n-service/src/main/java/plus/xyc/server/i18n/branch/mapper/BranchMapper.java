package plus.xyc.server.i18n.branch.mapper;
import java.util.Collection;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import plus.xyc.server.i18n.branch.entity.dto.Branch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.i18n.branch.entity.request.BranchAllRequest;
import plus.xyc.server.i18n.branch.entity.request.BranchListRequest;

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

    List<Branch> all(@Param("request") BranchAllRequest request);
    List<Branch> list(IPage<Branch> page, @Param("request") BranchListRequest request);
    int countByModuleIdAndName(@Param("moduleId") Long moduleId, @Param("name") String name);
    Branch findDefaultByModuleId(@Param("moduleId") Long moduleId);
    List<Branch> findByNameIn(@Param("moduleId") Long moduleId, @Param("nameList") Collection<String> nameList);
    List<Branch> findByModuleId(@Param("moduleId") Long moduleId);
    Branch findOneById(@Param("id") Long id);

}
