package plus.xyc.server.main.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import plus.xyc.server.main.api.constant.MainApiRoute;
import plus.xyc.server.main.api.entity.request.JoinProjectRequest;
import plus.xyc.server.main.project.service.ProjectService;

@RestController
@Slf4j
@Tag(name = "internal-project", description = "[内部接口]项目")
public class InternalProjectController {

    @Resource
    private ProjectService projectService;

    @Operation(summary = "接入项目")
    @PostMapping(MainApiRoute.JOIN_PROJECT)
    public Boolean joinProject(@RequestBody JoinProjectRequest request) {
        return projectService.join(request);
    }

}
