package plus.xyc.server.main.project.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.project.entity.response.ProjectResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapStruct {

    ProjectResponse toProjectResponse(Project project);

}
