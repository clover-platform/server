package plus.xyc.server.i18n.member.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import plus.xyc.server.i18n.member.entity.request.MemberInviteGenerateRequest;
import plus.xyc.server.i18n.member.entity.request.MemberInviteSendRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberInviteMapStruct {

    MemberInviteGenerateRequest toMemberInviteGenerateRequest(MemberInviteSendRequest request);

}
