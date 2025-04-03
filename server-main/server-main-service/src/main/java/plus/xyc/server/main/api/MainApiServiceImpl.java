package plus.xyc.server.main.api;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import plus.xyc.server.main.api.entity.request.JoinProjectRequest;
import plus.xyc.server.main.api.service.MainApiService;
import plus.xyc.server.main.project.service.ProjectService;

@DubboService
public class MainApiServiceImpl implements MainApiService {

    @Resource
    private ProjectService projectService;

    @Override
    public Boolean joinProject(JoinProjectRequest request) {
        return projectService.join(request);
    }
}
