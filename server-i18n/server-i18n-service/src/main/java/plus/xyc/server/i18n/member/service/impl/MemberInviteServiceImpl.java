package plus.xyc.server.i18n.member.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.zkit.support.server.mail.api.ServerMailApi;
import org.zkit.support.server.mail.api.entity.MailSendRequest;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MD5Utils;
import org.zkit.support.starter.boot.utils.MessageUtils;
import plus.xyc.server.i18n.configuration.InviteConfiguration;
import plus.xyc.server.i18n.enums.I18nCode;
import plus.xyc.server.i18n.member.entity.dto.MemberInvite;
import plus.xyc.server.i18n.member.entity.enums.MemberRoleType;
import plus.xyc.server.i18n.member.entity.mapstruct.MemberInviteMapStruct;
import plus.xyc.server.i18n.member.entity.request.MemberInviteGenerateRequest;
import plus.xyc.server.i18n.member.entity.request.MemberInviteRequest;
import plus.xyc.server.i18n.member.entity.request.MemberInviteRevokeRequest;
import plus.xyc.server.i18n.member.entity.request.MemberInviteSendRequest;
import plus.xyc.server.i18n.member.mapper.MemberInviteMapper;
import plus.xyc.server.i18n.member.service.MemberInviteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.module.service.ModuleAccessService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * <p>
 * 邀请记录 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
@Slf4j
public class MemberInviteServiceImpl extends ServiceImpl<MemberInviteMapper, MemberInvite> implements MemberInviteService {

    @Resource
    private ModuleAccessService moduleAccessService;
    @Resource
    private ServerMailApi serverMailApi;
    @Resource
    private InviteConfiguration configuration;
    @Resource
    private MemberInviteMapStruct struct;

    @Override
    public List<MemberInvite> query(MemberInviteRequest request) {
        return baseMapper.query(request);
    }

    @Override
    public String generate(MemberInviteGenerateRequest request) {
        List<Integer> adminRoles = List.of(MemberRoleType.ADMIN.code, MemberRoleType.OWNER.code);
        boolean checked = moduleAccessService.check(request.getModuleId(), request.getUserId(), adminRoles);
        if(!checked) {
            throw new ResultException(I18nCode.ACCESS_ERROR.code, MessageUtils.get(I18nCode.ACCESS_ERROR.key));
        }
        MemberInvite invite = new MemberInvite();
        invite.setToken(MD5Utils.text(UUID.randomUUID().toString()));
        invite.setModuleId(request.getModuleId());
        invite.setRoles(request.getRoles());
        invite.setCreatorId(request.getUserId());
        save(invite);
        return invite.getToken();
    }

    @Override
    public void send(MemberInviteSendRequest request) {
        log.info("send invite: {}", request);
        String token = this.generate(struct.toMemberInviteGenerateRequest(request));
        String url = configuration.getBase() + token;
        MailSendRequest mail = new MailSendRequest();
        mail.setTemplate("i18n/invite");
        mail.setLanguage(MessageUtils.getLocale());
        Map<String, Object> data = new HashMap<>();
        data.put("url", url);
        data.put("content", request.getContent());
        mail.setData(data);
        Stream.of(request.getEmails().split(",")).forEach(email -> {
            mail.setTo(email);
            serverMailApi.send(mail);
        });
    }

    @Override
    public void revoke(MemberInviteRevokeRequest request) {
        List<Integer> adminRoles = List.of(MemberRoleType.ADMIN.code, MemberRoleType.OWNER.code);
        boolean checked = moduleAccessService.check(request.getModuleId(), request.getUserId(), adminRoles);
        if(!checked) {
            throw new ResultException(I18nCode.ACCESS_ERROR.code, MessageUtils.get(I18nCode.ACCESS_ERROR.key));
        }
        removeById(request.getId());
    }
}
