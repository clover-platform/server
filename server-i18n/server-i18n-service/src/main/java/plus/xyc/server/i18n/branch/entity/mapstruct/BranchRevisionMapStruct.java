package plus.xyc.server.i18n.branch.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import plus.xyc.server.i18n.branch.entity.dto.BranchRevision;
import plus.xyc.server.i18n.branch.entity.request.NewRevisionRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BranchRevisionMapStruct {

    @Mapping(target = "createUser", source = "userId")
    BranchRevision toBranchRevision(NewRevisionRequest request);

}
