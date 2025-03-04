package plus.xyc.server.main.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.main.team.entity.request.CreateTeamRequest;
import plus.xyc.server.main.team.entity.request.InitTeamRequest;
import plus.xyc.server.main.team.entity.request.TeamListRequest;
import plus.xyc.server.main.team.entity.response.InitTeamResponse;
import plus.xyc.server.main.team.entity.response.TeamListResponse;
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
@Tag(name = "TeamController", description = "团队")
public class TeamController {

    @Resource
    private TeamService teamService;

    @GetMapping("/my")
    @Operation(summary = "我的团队")
    public List<TeamListResponse> my(@CurrentUser() @Parameter(hidden = true) SessionUser user) {
        return teamService.my(user.getId());
    }

    @PostMapping("/init")
    @Operation(summary = "初始化团队")
    public InitTeamResponse init(
            @RequestBody InitTeamRequest request,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        request.setOwnerId(user.getId());
        return teamService.init(request);
    }

    @PostMapping("/create")
    @Operation(summary = "创建团队")
    public void create(
            @RequestBody CreateTeamRequest request,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        request.setOwnerId(user.getId());
        teamService.create(request);
    }

    @GetMapping("/list")
    @Operation(summary = "我的团队")
    public PageResult<TeamListResponse> list(
            @ParameterObject @ModelAttribute PageRequest page,
            @ParameterObject @ModelAttribute TeamListRequest request,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        request.setUserId(user.getId());
        return teamService.list(page, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除团队")
    public void delete(
            @Parameter(description = "团队 ID") @PathVariable Long id,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        teamService.delete(id, user.getId());
    }

}
