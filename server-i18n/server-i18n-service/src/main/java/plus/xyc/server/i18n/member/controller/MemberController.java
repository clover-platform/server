package plus.xyc.server.i18n.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.activity.entity.dto.Activity;
import plus.xyc.server.i18n.activity.entity.request.ActivityListRequest;
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
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @GetMapping("/list")
    @Operation(summary = "列表")
    public PageResult<MemberResponse> list(
            @ParameterObject @ModelAttribute PageQueryRequest page,
            @ParameterObject @ModelAttribute MemberListRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setUserId(user.getId());
        return memberService.query(page, request);
    }

}
