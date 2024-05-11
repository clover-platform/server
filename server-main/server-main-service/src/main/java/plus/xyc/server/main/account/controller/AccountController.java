package plus.xyc.server.main.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.boot.auth.annotation.CurrentUser;
import org.zkit.support.starter.boot.auth.annotation.PublicRequest;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.entity.SessionUser;
import org.zkit.support.server.account.api.entity.request.SetPasswordRequest;
import org.zkit.support.server.account.api.entity.response.AccountResponse;
import org.zkit.support.server.account.api.entity.response.OTPResponse;
import org.zkit.support.server.account.api.entity.response.TokenResponse;
import org.zkit.support.server.account.api.rest.AuthAccountApi;
import org.zkit.support.starter.throttler.annotation.Throttler;
import plus.xyc.server.main.account.entity.request.CheckRegisterEmailRequest;
import plus.xyc.server.main.account.entity.request.SendRegisterEmailRequest;
import plus.xyc.server.main.account.entity.request.TestCheckRequest;
import plus.xyc.server.main.account.service.AccountService;

/**
 * <p>
 * 用户前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-02
 */
@RestController
@RequestMapping("/account")
@Slf4j
@Validated
@Tag(name = "account", description = "用户账户")
public class AccountController {

    private AccountService accountService;
    private AuthAccountApi authAccountApi;

    @PublicRequest
    @GetMapping("/test")
    @Operation(summary = "测试")
    public AccountResponse test(@RequestParam("username") @Parameter(description = "用户名") String username) {
        Result<AccountResponse> response = authAccountApi.findByUsername(username);
        log.info("AccountController test {}", response);
        return response.getData();
    }

    @PublicRequest
    @PostMapping("/test/check")
    @Operation(summary = "测试表单验证")
    public AccountResponse testCheck(@RequestBody @Validated TestCheckRequest request) {
        log.info("AccountController testCheck {}", request);
        return null;
    }

    @PublicRequest
    @PostMapping("/register/email/send")
    @Operation(summary = "发送邮件验证码")
    public void sendRegisterEmail(@RequestBody SendRegisterEmailRequest request) {
        this.accountService.sendRegisterEmail(request.getEmail());
    }

    @PublicRequest
    @PostMapping("/register/email/check")
    @Throttler(value = "register", limit = 5)
    @Operation(summary = "校验邮件")
    public TokenResponse checkRegisterEmail(@RequestBody CheckRegisterEmailRequest request) {
        return this.accountService.checkRegisterEmail(request);
    }

    @GetMapping("/otp/secret")
    @Operation(summary = "获取秘钥")
    public OTPResponse otpSecret(@CurrentUser() @Parameter(hidden = true) SessionUser user) {
        log.info("AccountController otpSecret {}", user);
        Result<OTPResponse> response = authAccountApi.otpSecret(user.getId());
        return response.getData();
    }

    @PostMapping("/register/password/set")
    @Operation(summary = "注册设置密码")
    public TokenResponse setPassword(
            @CurrentUser() @Parameter(hidden = true) SessionUser user,
            @RequestBody SetPasswordRequest request
    ) {
        request.setId(user.getId());
        return this.accountService.setPassword(request);
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setAuthAccountApi(AuthAccountApi authAccountApi) {
        this.authAccountApi = authAccountApi;
    }
}
