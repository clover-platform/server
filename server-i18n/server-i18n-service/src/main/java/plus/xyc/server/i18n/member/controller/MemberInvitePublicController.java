package plus.xyc.server.i18n.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.annotation.PublicRequest;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.member.entity.request.MemberInviteAcceptRequest;
import plus.xyc.server.i18n.member.entity.response.MemberInviteDetailResponse;
import plus.xyc.server.i18n.member.service.MemberInviteService;

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
@Tag(name = "memberInvitePublic", description = "成员邀请信息")
@Slf4j
public class MemberInvitePublicController {

    @Resource
    private MemberInviteService memberInviteService;

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
    public String accept(
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @RequestBody MemberInviteAcceptRequest request
    ) {
        request.setId(user.getId());
        return memberInviteService.accept(request);
    }

}
