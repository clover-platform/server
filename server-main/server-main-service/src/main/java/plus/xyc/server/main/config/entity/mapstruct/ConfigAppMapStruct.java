package plus.xyc.server.main.config.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import plus.xyc.server.main.config.entity.dto.ConfigApp;
import plus.xyc.server.main.config.entity.response.AppResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConfigAppMapStruct {

    AppResponse toAppResponse(ConfigApp app);

}
