package plus.xyc.server.main.project.controller;

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
import plus.xyc.server.main.project.entity.request.CreateProjectRequest;
import plus.xyc.server.main.project.entity.request.ProjectListRequest;
import plus.xyc.server.main.project.entity.response.ProjectResponse;
import plus.xyc.server.main.project.service.ProjectService;

import java.util.List;

/**
 * <p>
 * 项目 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/project")
@Tag(name = "ProjectController", description = "项目")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @GetMapping("/my")
    @Operation(summary = "我的项目")
    public List<ProjectResponse> my(
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        return projectService.my(user.getId());
    }

    @GetMapping("/list")
    @Operation(summary = "我的项目")
    public PageResult<ProjectResponse> list(
            @ParameterObject @ModelAttribute PageRequest page,
            @ParameterObject @ModelAttribute ProjectListRequest request,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        request.setUserId(user.getId());
        return projectService.list(page, request);
    }

    @PostMapping("/create")
    @Operation(summary = "创建项目")
    public void create(
            @RequestBody CreateProjectRequest request,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        request.setOwnerId(user.getId());
        projectService.create(request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除项目")
    public void delete(
            @Parameter(description = "项目 ID") @PathVariable Long id,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        projectService.delete(id, user.getId());
    }

    @DeleteMapping("/{id}/leave")
    @Operation(summary = "退出项目")
    public void leave(
            @Parameter(description = "项目 ID") @PathVariable Long id,
            @CurrentUser() @Parameter(hidden = true) SessionUser user) {
        projectService.leave(id, user.getId());
    }

}
