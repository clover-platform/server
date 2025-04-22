package plus.xyc.server.i18n.bundle.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import plus.xyc.server.i18n.bundle.entity.dto.Bundle;
import plus.xyc.server.i18n.bundle.entity.request.BundleCreateRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BundleMapStruct {

    @Mapping(target = "formatConfig", source = "export.config")
    @Mapping(target = "format", source = "export.format")
    Bundle toBundle(BundleCreateRequest request);

}
