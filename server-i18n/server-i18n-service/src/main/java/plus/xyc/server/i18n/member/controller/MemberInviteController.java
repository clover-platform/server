package plus.xyc.server.i18n.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.annotation.PublicRequest;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.member.entity.dto.MemberInvite;
import plus.xyc.server.i18n.member.entity.request.*;
import plus.xyc.server.i18n.member.entity.response.MemberInviteDetailResponse;
import plus.xyc.server.i18n.member.service.MemberInviteService;

import java.util.List;

/**
 * <p>
 * 邀请记录 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/member/invite")
@Slf4j
public class MemberInviteController {

    @Resource
    private MemberInviteService memberInviteService;

    @GetMapping("/list")
    @Operation(summary = "列表")
    public List<MemberInvite> list(@ParameterObject @ModelAttribute MemberInviteRequest request) {
        return memberInviteService.query(request);
    }

    @PostMapping("/generate")
    @Operation(summary = "生成链接")
    public String generate(
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @RequestBody MemberInviteGenerateRequest request
    ) {
        request.setUserId(user.getId());
        return memberInviteService.generate(request);
    }

    @PostMapping("/send")
    @Operation(summary = "发送邀请链接")
    public void send(
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @RequestBody MemberInviteSendRequest request
    ) {
        request.setUserId(user.getId());
        memberInviteService.send(request);
    }

    @DeleteMapping("/revoke")
    @Operation(summary = "撤销邀请链接")
    public void revoke(
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @RequestBody MemberInviteRevokeRequest request
    ) {
        request.setUserId(user.getId());
        memberInviteService.revoke(request);
    }

    @PublicRequest
    @GetMapping("/detail/{token}")
    @Operation(summary = "邀请详情")
    public MemberInviteDetailResponse detail(
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @Parameter(description = "邀请码") @PathVariable String token
    ) {
        return memberInviteService.detail(user != null ? user.getId() : null, token);
    }

    @PostMapping("/accept")
    @Operation(summary = "接受邀请")
    public Long accept(
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @RequestBody MemberInviteAcceptRequest request
    ) {
        request.setId(user.getId());
        return memberInviteService.accept(request);
    }

}
