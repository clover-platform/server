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
import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.project.entity.request.ProjectListRequest;
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
@Tag(name = "project", description = "项目")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @GetMapping("/my")
    @Operation(summary = "我的项目")
    public List<Project> my(
            @RequestParam(required = false) Long teamId,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        return projectService.my(user.getId(), teamId);
    }

    @GetMapping("/list")
    @Operation(summary = "我的项目")
    public PageResult<Project> list(
            @ParameterObject @ModelAttribute PageRequest page,
            @ParameterObject @ModelAttribute ProjectListRequest request,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        request.setUserId(user.getId());
        return projectService.list(page, request);
    }

}
