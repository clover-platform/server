package plus.xyc.server.i18n.member.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.zkit.support.server.mail.api.ServerMailApi;
import org.zkit.support.server.mail.api.entity.MailSendRequest;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MD5Utils;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.configuration.InviteConfiguration;
import plus.xyc.server.i18n.enums.I18nCode;
import plus.xyc.server.i18n.member.entity.dto.Member;
import plus.xyc.server.i18n.member.entity.dto.MemberInvite;
import plus.xyc.server.i18n.member.entity.dto.MemberRole;
import plus.xyc.server.i18n.member.entity.enums.MemberRoleType;
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
import plus.xyc.server.i18n.module.service.ModuleAccessService;
import plus.xyc.server.i18n.module.service.ModuleService;
import plus.xyc.server.main.api.entity.request.JoinProjectRequest;
import plus.xyc.server.main.api.rest.MainRestApi;

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
    private ModuleAccessService moduleAccessService;
    @Resource
    private ServerMailApi serverMailApi;
    @Resource
    private MainRestApi mainRestApi;
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

    @Override
    public MemberInviteDetailResponse detail(Long userId, String token) {
        MemberInvite invite = baseMapper.findOneByToken(token);
        Long time = configuration.getExpireAt();
        if(System.currentTimeMillis() - invite.getCreateTime().getTime() >= time * 1000) {
            throw new ResultException(I18nCode.MEMBER_INVITE_EXPIRED.code, MessageUtils.get(I18nCode.MEMBER_INVITE_EXPIRED.key));
        }
        List<Member> members = memberMapper.findByModuleId(invite.getModuleId());
        List<Long> memberIds = members.stream().map(Member::getAccountId).toList();
        if(memberIds.contains(userId)) {
            ResultException exception = new ResultException(I18nCode.MEMBER_JOINED.code, MessageUtils.get(I18nCode.MEMBER_JOINED.key));
            exception.setData(invite.getModuleId());
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
    public Long accept(MemberInviteAcceptRequest request) {
        MemberInvite invite = baseMapper.findOneByToken(request.getToken());
        Module module = moduleService.getById(invite.getModuleId());
        int size = memberMapper.countByModuleIdAndAccountId(invite.getModuleId(), request.getId());
        if(size > 0) {
            throw new ResultException(I18nCode.MEMBER_JOINED.code, MessageUtils.get(I18nCode.MEMBER_JOINED.key));
        }
        JoinProjectRequest joinProjectRequest = new JoinProjectRequest();
        joinProjectRequest.setProjectId(module.getProjectId());
        joinProjectRequest.setUserId(request.getId());
        Result<Boolean> result = mainRestApi.joinProject(joinProjectRequest);
        if(!result.isSuccess()) {
            throw new ResultException(result.getCode(), result.getMessage());
        }
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
        return module.getId();
    }
}
