package plus.xyc.server.main.project.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Collection;
import java.util.List;

import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.project.entity.dto.ProjectCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 收藏的项目 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2025-03-01
 */
public interface ProjectCollectMapper extends BaseMapper<ProjectCollect> {

    List<Project> my(@Param("userId") Long userId, @Param("limit") Integer limit);
    List<ProjectCollect> findByProjectIdInAndUserId(@Param("projectIdList") Collection<Long> projectIdList, @Param("userId") Long userId);

}
