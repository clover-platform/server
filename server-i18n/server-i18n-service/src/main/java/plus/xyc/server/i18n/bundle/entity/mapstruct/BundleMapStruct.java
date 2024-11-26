package plus.xyc.server.i18n.bundle.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import plus.xyc.server.i18n.bundle.entity.dto.Bundle;
import plus.xyc.server.i18n.bundle.entity.request.BundleCreateRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BundleMapStruct {

    @Mapping(target = "formatConfig", source = "export.config")
    @Mapping(target = "format", source = "export.format")
    Bundle toBundle(BundleCreateRequest request);

}
