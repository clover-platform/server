package plus.xyc.server.wiki.page.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import plus.xyc.server.wiki.page.entity.dto.PageContent;
import plus.xyc.server.wiki.page.entity.response.PageDetailResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PageContentStruct {

    PageDetailResponse toPageDetailResponse(PageContent pageContent);

}
