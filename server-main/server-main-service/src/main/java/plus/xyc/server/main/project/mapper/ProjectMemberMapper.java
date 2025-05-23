package plus.xyc.server.main.project.mapper;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.main.project.entity.dto.ProjectMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 项目成员 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
public interface ProjectMemberMapper extends BaseMapper<ProjectMember> {

    int countByAccountIdAndProjectId(@Param("accountId") Long accountId, @Param("projectId") Long projectId);

}
