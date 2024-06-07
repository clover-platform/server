package plus.xyc.server.i18n.member.service;

import plus.xyc.server.i18n.member.entity.dto.MemberInvite;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.member.entity.request.MemberInviteGenerateRequest;
import plus.xyc.server.i18n.member.entity.request.MemberInviteRequest;
import plus.xyc.server.i18n.member.entity.request.MemberInviteSendRequest;

import java.util.List;

/**
 * <p>
 * 邀请记录 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface MemberInviteService extends IService<MemberInvite> {

    List<MemberInvite> query(MemberInviteRequest request);
    String generate(MemberInviteGenerateRequest request);
    void send(MemberInviteSendRequest request);

}
