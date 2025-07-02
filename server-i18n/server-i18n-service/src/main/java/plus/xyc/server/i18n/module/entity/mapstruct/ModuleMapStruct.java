package plus.xyc.server.i18n.module.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import plus.xyc.server.i18n.module.entity.dto.Module;
import plus.xyc.server.i18n.module.entity.response.ModuleResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModuleMapStruct {

    @Mapping(target = "ownerId", source = "owner")
    ModuleResponse toModuleResponse(Module module);

}
