package plus.xyc.server.main.team.service;

import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.main.team.entity.dto.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.main.team.entity.request.CreateTeamRequest;
import plus.xyc.server.main.team.entity.request.InitTeamRequest;
import plus.xyc.server.main.team.entity.request.TeamListRequest;
import plus.xyc.server.main.team.entity.response.InitTeamResponse;
import plus.xyc.server.main.team.entity.response.TeamListResponse;

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

    List<TeamListResponse> my(Long userId);
    InitTeamResponse init(InitTeamRequest request);
    void create(CreateTeamRequest request);
    PageResult<TeamListResponse> list(PageRequest pr, TeamListRequest request);
    void delete(Long id, Long userId);

}
