package plus.xyc.server.main.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.server.account.api.entity.response.OTPResponse;
import org.zkit.support.server.account.api.entity.response.OTPStatusResponse;
import org.zkit.support.server.account.api.service.AuthAccountOTPApiService;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.main.account.entity.request.OTPBindRequest;
import plus.xyc.server.main.account.entity.request.OTPDisableRequest;
import plus.xyc.server.main.account.service.AccountService;

@RestController
@RequestMapping("/account/otp")
@Slf4j
@Validated
@Tag(name = "AccountOTPController", description = "用户OTP设置")
public class AccountOTPController {

    @DubboReference
    private AuthAccountOTPApiService authAccountOTPApiService;
    @Resource
    private AccountService accountService;

    @GetMapping("/secret")
    @Operation(summary = "获取秘钥")
    public OTPResponse secret(@CurrentUser() @Parameter(hidden = true) SessionUser user) {
        return authAccountOTPApiService.otpSecret(user.getId());
    }

    @GetMapping("/status")
    @Operation(summary = "OTP状态")
    public OTPStatusResponse status(@CurrentUser() @Parameter(hidden = true) SessionUser user) {
        return authAccountOTPApiService.otpState(user.getId());
    }

    @PostMapping("/bind")
    @Operation(summary = "绑定OTP")
    public void bind(
            @RequestBody OTPBindRequest request,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        request.setAccountId(user.getId());
        accountService.bindOTP(request);
    }

    @PostMapping("/disable")
    @Operation(summary = "禁用OTP")
    public void disable(
            @RequestBody OTPDisableRequest request,
            @CurrentUser() @Parameter(hidden = true) SessionUser user
    ) {
        request.setAccountId(user.getId());
        accountService.disableOTP(request);
    }

}
