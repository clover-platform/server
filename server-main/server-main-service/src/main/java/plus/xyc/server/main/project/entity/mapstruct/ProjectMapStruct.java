package plus.xyc.server.main.project.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.project.entity.request.CreateProjectRequest;
import plus.xyc.server.main.project.entity.response.ProjectResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapStruct {

    ProjectResponse toProjectResponse(Project project);
    Project toProject(CreateProjectRequest request);

}
