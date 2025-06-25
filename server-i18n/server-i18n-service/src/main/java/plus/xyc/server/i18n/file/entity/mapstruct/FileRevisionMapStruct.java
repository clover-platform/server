package plus.xyc.server.i18n.file.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import plus.xyc.server.i18n.file.entity.dto.FileRevision;
import plus.xyc.server.i18n.file.entity.response.FileRevisionResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileRevisionMapStruct { 
    
    FileRevisionResponse toFileRevisionResponse(FileRevision fileRevision);

}
