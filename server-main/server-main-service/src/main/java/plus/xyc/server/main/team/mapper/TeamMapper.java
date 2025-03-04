package plus.xyc.server.main.team.mapper;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.main.team.entity.dto.Team;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.main.team.entity.response.TeamListResponse;

import java.util.List;

/**
 * <p>
 * 团队 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
public interface TeamMapper extends BaseMapper<Team> {

    List<TeamListResponse> findMy(@Param("userId") Long userId, @Param("keyword") String keyword);
    int countByTeamKeyAndDeleted(@Param("teamKey") String teamKey, @Param("deleted") Boolean deleted);
    List<TeamListResponse> findAllByUserId(@Param("userId") Long userId, @Param("keyword") String keyword);
    List<TeamListResponse> findJoin(@Param("userId") Long userId, @Param("keyword") String keyword);
    int countByIdAndOwnerId(@Param("id") Long id, @Param("ownerId") Long ownerId);

}
