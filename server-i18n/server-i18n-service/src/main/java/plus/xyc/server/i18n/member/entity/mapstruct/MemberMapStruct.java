package plus.xyc.server.i18n.member.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import plus.xyc.server.i18n.member.entity.dto.Member;
import plus.xyc.server.i18n.member.entity.response.MemberResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapStruct {

    MemberResponse toMemberResponse(Member member);

}
