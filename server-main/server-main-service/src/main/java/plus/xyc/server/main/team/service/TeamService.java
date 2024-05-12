package plus.xyc.server.main.team.service;

import plus.xyc.server.main.team.entity.dto.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.main.team.entity.request.InitTeamRequest;
import plus.xyc.server.main.team.entity.response.InitTeamResponse;

import java.util.List;

/**
 * <p>
 * 团队 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
public interface TeamService extends IService<Team> {

    List<Team> my(Long userId);
    InitTeamResponse init(InitTeamRequest request);

}
