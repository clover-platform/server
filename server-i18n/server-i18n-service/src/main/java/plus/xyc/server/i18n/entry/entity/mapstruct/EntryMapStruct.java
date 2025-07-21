package plus.xyc.server.i18n.entry.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.response.EntryResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithResultResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithStateResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntryMapStruct {

    EntryWithResultResponse toEntryWithResultResponse(Entry entry);
    Entry toEntryFromEntryWithResultResponse(EntryWithResultResponse entryWithResultResponse);
    EntryWithStateResponse toEntryWithStateResponse(Entry entry);
    EntryResponse toEntryResponse(Entry entry);

}
