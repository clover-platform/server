package plus.xyc.server.main.team.mapper;
import java.util.Collection;

import org.apache.ibatis.annotations.Param;
import plus.xyc.server.main.team.entity.dto.Team;
import plus.xyc.server.main.team.entity.dto.TeamCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 收藏的团队 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2025-03-01
 */
public interface TeamCollectMapper extends BaseMapper<TeamCollect> {

    List<Team> my(@Param("userId") Long userId);
    List<TeamCollect> findByTeamIdInAndUserId(@Param("teamIdList") Collection<Long> teamIdList, @Param("userId") Long userId);

}
