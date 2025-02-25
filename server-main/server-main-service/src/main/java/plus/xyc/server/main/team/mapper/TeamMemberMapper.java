package plus.xyc.server.main.team.mapper;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.main.team.entity.dto.TeamMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 团队成员 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
public interface TeamMemberMapper extends BaseMapper<TeamMember> {

    int countByAccountIdAndTeamId(@Param("accountId") Long accountId, @Param("teamId") Long teamId);

}
