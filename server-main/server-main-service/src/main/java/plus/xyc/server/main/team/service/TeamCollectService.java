package plus.xyc.server.main.team.service;

import plus.xyc.server.main.team.entity.dto.Team;
import plus.xyc.server.main.team.entity.dto.TeamCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.main.team.entity.request.TeamCollectRequest;

import java.util.List;

/**
 * <p>
 * 收藏的团队 服务类
 * </p>
 *
 * @author generator
 * @since 2025-03-01
 */
public interface TeamCollectService extends IService<TeamCollect> {

    void add(TeamCollectRequest request);
    void cancel(TeamCollectRequest request);
    List<Team> my(Long userId);
    List<TeamCollect> findByTeamIdsAndUserId(List<Long> teamIds, Long userId);

}
