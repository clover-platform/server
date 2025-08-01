package plus.xyc.server.i18n.member.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.zkit.support.server.message.api.entity.request.SendMailRequest;
import org.zkit.support.server.message.api.service.MailService;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MD5Utils;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.configuration.InviteConfiguration;
import plus.xyc.server.i18n.common.enums.I18nCode;
import plus.xyc.server.i18n.member.entity.dto.Member;
import plus.xyc.server.i18n.member.entity.dto.MemberInvite;
import plus.xyc.server.i18n.member.entity.dto.MemberRole;
import plus.xyc.server.i18n.member.entity.mapstruct.MemberInviteMapStruct;
import plus.xyc.server.i18n.member.entity.request.*;
import plus.xyc.server.i18n.member.entity.response.MemberInviteDetailResponse;
import plus.xyc.server.i18n.member.mapper.MemberInviteMapper;
import plus.xyc.server.i18n.member.mapper.MemberMapper;
import plus.xyc.server.i18n.member.mapper.MemberRoleMapper;
import plus.xyc.server.i18n.member.service.MemberInviteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.module.entity.dto.Module;
import plus.xyc.server.i18n.module.entity.response.ModuleDashboardResponse;
import plus.xyc.server.i18n.module.service.ModuleService;
import plus.xyc.server.main.api.entity.request.JoinTeamRequest;
import plus.xyc.server.main.api.service.MainApiService;

import java.util.*;
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
    private MailService mailService;
    @DubboReference
    private MainApiService mainApiService;
    @Resource
    private InviteConfiguration configuration;
    @Resource
    private MemberInviteMapStruct struct;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private ModuleService moduleService;
    @Resource
    private MemberRoleMapper memberRoleMapper;

    @Override
    public List<MemberInvite> query(MemberInviteRequest request) {
        return baseMapper.query(request);
    }

    @Override
    public String generate(MemberInviteGenerateRequest request) {
        // List<Integer> adminRoles = List.of(MemberRoleType.ADMIN.code, MemberRoleType.OWNER.code);
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
        SendMailRequest mail = new SendMailRequest();
        mail.setTemplate("i18n/invite");
        mail.setLanguage(MessageUtils.getLocale());
        Map<String, Object> data = new HashMap<>();
        data.put("url", url);
        data.put("content", request.getContent());
        mail.setData(data);
        Stream.of(request.getEmails().split(",")).forEach(email -> {
            mail.setTo(email);
            mailService.send(mail);
        });
    }

    @Override
    public void revoke(MemberInviteRevokeRequest request) {
        removeById(request.getId());
    }

    @Override
    public MemberInviteDetailResponse detail(Long userId, String token) {
        MemberInvite invite = baseMapper.findOneByToken(token);
        if(invite == null) {
            throw new ResultException(I18nCode.MEMBER_INVITE_EXPIRED.code, MessageUtils.get(I18nCode.MEMBER_INVITE_EXPIRED.key));
        }
        Long time = configuration.getExpireAt();
        if(System.currentTimeMillis() - invite.getCreateTime().getTime() >= time * 1000) {
            throw new ResultException(I18nCode.MEMBER_INVITE_EXPIRED.code, MessageUtils.get(I18nCode.MEMBER_INVITE_EXPIRED.key));
        }
        List<Member> members = memberMapper.findByModuleId(invite.getModuleId());
        List<Long> memberIds = members.stream().map(Member::getAccountId).toList();
        if(memberIds.contains(userId)) {
            ResultException exception = new ResultException(I18nCode.MEMBER_JOINED.code, MessageUtils.get(I18nCode.MEMBER_JOINED.key));
            Module module = moduleService.getById(invite.getModuleId());
            exception.setData(module.getIdentifier());
            throw exception;
        }
        ModuleDashboardResponse result = moduleService.dashboard(invite.getModuleId());
        MemberInviteDetailResponse response = struct.toMemberInviteDetailResponseFromModuleResponse(result.getDetail());
        response.setRoles(JSON.parseObject(JSON.toJSONString(invite.getRoles()),  new TypeReference<List<Long>>(){}));
        response.setTargets(result.getLanguages());
        return response;
    }

    @Override
    @Transactional
    @DistributedLock("'module:member:accept:'+#request.token")
    public String accept(MemberInviteAcceptRequest request) {
        MemberInvite invite = baseMapper.findOneByToken(request.getToken());
        Module module = moduleService.getById(invite.getModuleId());
        int size = memberMapper.countByModuleIdAndAccountId(invite.getModuleId(), request.getId());
        if(size > 0) {
            throw new ResultException(I18nCode.MEMBER_JOINED.code, MessageUtils.get(I18nCode.MEMBER_JOINED.key));
        }
        JoinTeamRequest joinProjectRequest = new JoinTeamRequest();
        joinProjectRequest.setTeamId(module.getTeamId());
        joinProjectRequest.setUserId(request.getId());
        mainApiService.joinProject(joinProjectRequest);
        Member member = new Member();
        member.setAccountId(request.getId());
        member.setModuleId(invite.getModuleId());
        member.setJoinTime(new Date());
        memberMapper.insert(member);
        List<Integer> roles = JSON.parseObject(JSON.toJSONString(invite.getRoles()),  new TypeReference<List<Integer>>(){});
        roles.forEach(role -> {
            MemberRole memberRole = new MemberRole();
            memberRole.setMemberId(member.getId());
            memberRole.setRole(role);
            memberRoleMapper.insert(memberRole);
        });
        return module.getIdentifier();
    }
}
