package plus.xyc.server.i18n.activity.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import plus.xyc.server.i18n.activity.entity.request.ActivityListRequest;
import plus.xyc.server.i18n.activity.entity.response.ActivityResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActivityMapStruct {

    org.zkit.support.server.message.api.entity.request.ActivityListRequest toActivityListRequest(ActivityListRequest activity);
    ActivityResponse toActivityResponse(org.zkit.support.server.message.api.entity.response.ActivityResponse activity);

}
