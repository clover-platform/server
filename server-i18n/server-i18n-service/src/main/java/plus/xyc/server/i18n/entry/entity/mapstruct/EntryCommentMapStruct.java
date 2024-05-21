package plus.xyc.server.i18n.entry.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import plus.xyc.server.i18n.entry.entity.dto.EntryComment;
import plus.xyc.server.i18n.entry.entity.response.EntryCommentResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntryCommentMapStruct {

    EntryCommentResponse toEntryCommentResponse(EntryComment comment);

}
