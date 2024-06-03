package plus.xyc.server.main.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.main.project.entity.dto.Project;
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

    private final ProjectService projectService;

    ProjectController(
            ProjectService projectService
    ) {
        this.projectService = projectService;
    }

    @GetMapping("/my")
    @Operation(summary = "我的项目")
    public List<Project> my(@CurrentUser() @Parameter(hidden = true) SessionUser user) {
        return projectService.my(user.getId());
    }

}
