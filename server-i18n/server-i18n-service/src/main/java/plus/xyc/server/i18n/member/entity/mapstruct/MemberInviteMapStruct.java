package plus.xyc.server.i18n.member.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import plus.xyc.server.i18n.member.entity.request.MemberInviteGenerateRequest;
import plus.xyc.server.i18n.member.entity.request.MemberInviteSendRequest;
import plus.xyc.server.i18n.member.entity.response.MemberInviteDetailResponse;
import plus.xyc.server.i18n.module.entity.response.ModuleResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberInviteMapStruct {

    MemberInviteGenerateRequest toMemberInviteGenerateRequest(MemberInviteSendRequest request);
    MemberInviteDetailResponse toMemberInviteDetailResponseFromModuleResponse(ModuleResponse request);

}
