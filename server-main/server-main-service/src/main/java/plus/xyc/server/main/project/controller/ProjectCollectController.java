package plus.xyc.server.main.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.project.entity.request.ProjectCollectRequest;
import plus.xyc.server.main.project.service.ProjectCollectService;

import java.util.List;

/**
 * <p>
 * 收藏的项目 前端控制器
 * </p>
 *
 * @author generator
 * @since 2025-03-01
 */
@RestController
@RequestMapping("/project/collect")
@Tag(name = "ProjectCollectController", description = "项目收藏")
public class ProjectCollectController {

    @Resource
    private ProjectCollectService projectCollectService;

    @GetMapping("/my")
    @Operation(summary = "我的收藏")
    public List<Project> my(@CurrentUser() @Parameter(hidden = true) SessionUser user) {
        return projectCollectService.my(user.getId());
    }

    @PostMapping("/add")
    @Operation(summary = "添加收藏")
    public void add(
            @RequestBody ProjectCollectRequest request,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        request.setUserId(user.getId());
        projectCollectService.add(request);
    }

    @DeleteMapping("/cancel")
    @Operation(summary = "取消收藏")
    public void cancel(
            @RequestBody ProjectCollectRequest request,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        request.setUserId(user.getId());
        projectCollectService.cancel(request);
    }

}
