package plus.xyc.server.i18n.entry.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.response.EntryWithResultResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithStateResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntryMapStruct {

    EntryWithResultResponse toEntryWithResultResponse(Entry entry);
    Entry toEntryFromEntryWithResultResponse(EntryWithResultResponse entryWithResultResponse);
    EntryWithStateResponse toEntryWithStateResponse(Entry entry);

}
