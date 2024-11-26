package plus.xyc.server.i18n.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.activity.entity.dto.Activity;
import plus.xyc.server.i18n.activity.entity.request.ActivityListRequest;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.member.entity.request.MemberListRequest;
import plus.xyc.server.i18n.member.entity.response.MemberResponse;
import plus.xyc.server.i18n.member.service.MemberService;

/**
 * <p>
 * 成员 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/{moduleName}/member")
@Tag(name = "member", description = "成员")
public class MemberController {

    @Resource
    private MemberService memberService;

    @GetMapping("/list")
    @Operation(summary = "列表")
    public PageResult<MemberResponse> list(
            @ParameterObject @ModelAttribute PageRequest page,
            @ParameterObject @ModelAttribute MemberListRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest
    ) {
        request.setUserId(user.getId());
        request.setModuleId(pathRequest.getModule().getId());
        return memberService.query(page, request);
    }

}
