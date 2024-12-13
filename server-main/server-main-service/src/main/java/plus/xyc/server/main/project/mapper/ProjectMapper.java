package plus.xyc.server.main.project.mapper;

import org.springframework.data.repository.query.Param;
import plus.xyc.server.main.project.entity.dto.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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

    List<Project> findJoin(@Param("userId") Long userId, @Param("teamId") Long teamId);
    List<Project> findAllByUserId(@Param("userId") Long userId, @Param("teamId") Long teamId);
    List<Project> findMy(@Param("userId") Long userId, @Param("teamId") Long teamId);

}
