package plus.xyc.server.main.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.server.account.api.entity.request.AccountLoginRequest;
import org.zkit.support.server.account.api.rest.AuthAccountRestApi;
import org.zkit.support.starter.boot.auth.annotation.CurrentUser;
import org.zkit.support.starter.boot.auth.annotation.PublicRequest;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.entity.SessionUser;
import org.zkit.support.server.account.api.entity.request.SetPasswordRequest;
import org.zkit.support.server.account.api.entity.response.AccountResponse;
import org.zkit.support.server.account.api.entity.response.OTPResponse;
import org.zkit.support.server.account.api.entity.response.TokenResponse;
import org.zkit.support.starter.throttler.annotation.Throttler;
import plus.xyc.server.main.account.entity.dto.Account;
import plus.xyc.server.main.account.entity.mapstruct.AccountMapStruct;
import plus.xyc.server.main.account.entity.request.CheckRegisterEmailRequest;
import plus.xyc.server.main.account.entity.request.SendRegisterEmailRequest;
import plus.xyc.server.main.account.entity.request.TestCheckRequest;
import plus.xyc.server.main.account.entity.response.AccountProfileResponse;
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

    @Resource
    private AccountService accountService;
    @Resource
    private AuthAccountRestApi authAccountRestApi;
    @Resource
    private AccountMapStruct accountMapStruct;

    @PublicRequest
    @GetMapping("/test")
    @Operation(summary = "测试")
    public AccountResponse test(@RequestParam("username") @Parameter(description = "用户名") String username) {
        Result<AccountResponse> response = authAccountRestApi.findByUsername(username);
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
        Result<OTPResponse> response = authAccountRestApi.otpSecret(user.getId());
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

    @GetMapping("/profile")
    @Operation(summary = "当前用户信息")
    public AccountProfileResponse profile(@CurrentUser() @Parameter(hidden = true) SessionUser user) {
        Account account = accountService.findById(user.getId());
        AccountProfileResponse response = accountMapStruct.toAccountProfileResponse(account);
        response.setAuthorities(user.getAuthorities());
        return response;
    }

    @PublicRequest
    @PostMapping("/login")
    @Operation(summary = "登录")
    public TokenResponse login(@RequestBody @Validated AccountLoginRequest request) {
        return accountService.login(request);
    }

    @PostMapping("/logout")
    @Operation(summary = "退出")
    public void logout(
            @CurrentUser() @Parameter(hidden = true) SessionUser user,
            @RequestHeader("Authorization") String token
    ) {
        accountService.logout(token.replaceAll("Bearer ", ""), user.getId());
    }
}
