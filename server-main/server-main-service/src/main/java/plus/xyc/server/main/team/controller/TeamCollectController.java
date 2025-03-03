package plus.xyc.server.main.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.main.team.entity.dto.Team;
import plus.xyc.server.main.team.entity.request.TeamCollectRequest;
import plus.xyc.server.main.team.service.TeamCollectService;

import java.util.List;

/**
 * <p>
 * 收藏的团队 前端控制器
 * </p>
 *
 * @author generator
 * @since 2025-03-01
 */
@RestController
@RequestMapping("/team/collect")
@Tag(name = "TeamCollectController", description = "团队收藏")
public class TeamCollectController {

    @Resource
    private TeamCollectService teamCollectService;

    @GetMapping("/my")
    @Operation(summary = "我的收藏")
    public List<Team> my(@CurrentUser() @Parameter(hidden = true) SessionUser user) {
        return teamCollectService.my(user.getId());
    }

    @PostMapping("/add")
    @Operation(summary = "添加收藏")
    public void add(
            @RequestBody TeamCollectRequest request,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        request.setUserId(user.getId());
        teamCollectService.add(request);
    }

    @DeleteMapping("/cancel")
    @Operation(summary = "取消收藏")
    public void cancel(
            @RequestBody TeamCollectRequest request,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        request.setUserId(user.getId());
        teamCollectService.cancel(request);
    }

}
