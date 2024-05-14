package plus.xyc.server.i18n.language.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import plus.xyc.server.i18n.language.entity.response.LanguageResponse;
import plus.xyc.server.i18n.module.entity.response.ModuleLanguageResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LanguageMapStruct {

    ModuleLanguageResponse toModuleLanguageResponse(LanguageResponse response);

}
