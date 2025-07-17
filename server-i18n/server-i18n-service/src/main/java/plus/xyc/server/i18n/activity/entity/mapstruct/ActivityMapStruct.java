package plus.xyc.server.i18n.activity.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import plus.xyc.server.i18n.activity.entity.request.ActivityListRequest;
import plus.xyc.server.i18n.activity.entity.response.ActivityResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActivityMapStruct {

    @Mapping(target = "metadata", ignore = true)
    org.zkit.support.server.message.api.entity.request.ActivityListRequest toActivityListRequest(ActivityListRequest activity);

    @Mapping(target = "account", ignore = true)
    ActivityResponse toActivityResponse(org.zkit.support.server.message.api.entity.response.ActivityResponse activity);

}
