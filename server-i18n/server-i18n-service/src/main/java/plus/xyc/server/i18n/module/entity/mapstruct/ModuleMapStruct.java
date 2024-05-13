package plus.xyc.server.i18n.module.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import plus.xyc.server.i18n.module.entity.dto.Module;
import plus.xyc.server.i18n.module.entity.response.ModuleResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ModuleMapStruct {

    ModuleResponse toModuleResponse(Module module);

}
