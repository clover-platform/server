package plus.xyc.server.wiki.page.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import plus.xyc.server.wiki.page.entity.dto.Page;
import plus.xyc.server.wiki.page.entity.response.CatalogResponse;
import plus.xyc.server.wiki.page.entity.response.PageDetailResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PageStruct {

    CatalogResponse toCatalogResponse(Page page);
    PageDetailResponse toPageDetailResponse(Page page);

}
