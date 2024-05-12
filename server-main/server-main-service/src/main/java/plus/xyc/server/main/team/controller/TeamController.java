package plus.xyc.server.main.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.boot.auth.annotation.CurrentUser;
import org.zkit.support.starter.boot.entity.SessionUser;
import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.project.service.ProjectService;
import plus.xyc.server.main.team.entity.dto.Team;
import plus.xyc.server.main.team.entity.request.InitTeamRequest;
import plus.xyc.server.main.team.entity.response.InitTeamResponse;
import plus.xyc.server.main.team.service.TeamService;

import java.util.List;

/**
 * <p>
 * 团队 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/team")
@Tag(name = "team", description = "团队")
public class TeamController {

    private final TeamService teamService;

    TeamController(
            TeamService teamService
    ) {
        this.teamService = teamService;
    }

    @GetMapping("/my")
    @Operation(summary = "我的项目")
    public List<Team> my(@CurrentUser() @Parameter(hidden = true) SessionUser user) {
        return teamService.my(user.getId());
    }

    @PostMapping("/init")
    @Operation(summary = "初始化项目")
    public InitTeamResponse init(
            @RequestBody InitTeamRequest request,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        request.setOwnerId(user.getId());
        return teamService.init(request);
    }

}
