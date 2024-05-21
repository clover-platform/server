package plus.xyc.server.i18n.entry.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.entity.response.EntryResultResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntryResultMapStruct {

    EntryResultResponse toEntryResultResponse(EntryResult result);

}
