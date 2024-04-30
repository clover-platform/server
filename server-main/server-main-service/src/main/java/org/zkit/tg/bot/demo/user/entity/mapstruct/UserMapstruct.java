package org.zkit.tg.bot.demo.user.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.zkit.tg.bot.demo.user.entity.dto.User;
import org.zkit.tg.bot.demo.user.entity.response.UserResponse;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapstruct {

    UserMapstruct INSTANCE = Mappers.getMapper(UserMapstruct.class);
    UserResponse toUserResponse(User user);

}
