package plus.xyc.server.main.config.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import plus.xyc.server.main.config.entity.dto.ConfigApp;
import plus.xyc.server.main.config.entity.reponse.AppResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConfigAppMapStruct {

    AppResponse toAppResponse(ConfigApp app);

}
