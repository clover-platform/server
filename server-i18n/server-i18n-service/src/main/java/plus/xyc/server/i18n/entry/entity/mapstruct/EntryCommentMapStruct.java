package plus.xyc.server.i18n.entry.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import plus.xyc.server.i18n.entry.entity.dto.EntryComment;
import plus.xyc.server.i18n.entry.entity.response.EntryCommentResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntryCommentMapStruct {

    EntryCommentResponse toEntryCommentResponse(EntryComment comment);

}
