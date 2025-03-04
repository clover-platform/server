package plus.xyc.server.main.project.mapper;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.main.project.entity.dto.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.main.project.entity.response.ProjectResponse;

import java.util.List;

/**
 * <p>
 * 项目 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
public interface ProjectMapper extends BaseMapper<Project> {

    List<ProjectResponse> findJoin(@Param("userId") Long userId, @Param("teamId") Long teamId, @Param("keyword") String keyword);
    List<ProjectResponse> findAllByUserId(@Param("userId") Long userId, @Param("teamId") Long teamId, @Param("keyword") String keyword);
    List<ProjectResponse> findMy(@Param("userId") Long userId, @Param("teamId") Long teamId, @Param("keyword") String keyword);
    int countByProjectKeyAndDeleted(@Param("projectKey") String projectKey, @Param("deleted") Boolean deleted);

}
