package plus.xyc.server.main.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.server.account.api.entity.response.OTPResponse;
import org.zkit.support.server.account.api.entity.response.OTPStatusResponse;
import org.zkit.support.server.account.api.rest.AuthAccountOTPRestApi;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;

@RestController
@RequestMapping("/account/otp")
@Slf4j
@Validated
@Tag(name = "account", description = "用户账户")
public class AccountOTPController {

    @Resource
    private AuthAccountOTPRestApi authAccountOTPRestApi;

    @GetMapping("/secret")
    @Operation(summary = "获取秘钥")
    public Result<OTPResponse> secret(@CurrentUser() @Parameter(hidden = true) SessionUser user) {
        return authAccountOTPRestApi.otpSecret(user.getId());
    }

    @GetMapping("/status")
    @Operation(summary = "OTP状态")
    public Result<OTPStatusResponse> status(@CurrentUser() @Parameter(hidden = true) SessionUser user) {
        return authAccountOTPRestApi.otpState(user.getId());
    }

}
