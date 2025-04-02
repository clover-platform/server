package plus.xyc.server.main.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.server.account.api.entity.request.AccountLoginRequest;
import org.zkit.support.server.account.api.entity.request.ChangePasswordRequest;
import org.zkit.support.server.account.api.entity.request.ResetPasswordRequest;
import org.zkit.support.server.account.api.entity.response.TokenResponse;
import org.zkit.support.server.account.api.service.AuthAccountApiService;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.annotation.PublicRequest;
import org.zkit.support.starter.security.entity.SessionUser;
import org.zkit.support.starter.throttler.annotation.Throttler;
import plus.xyc.server.main.account.entity.dto.Account;
import plus.xyc.server.main.account.entity.mapstruct.AccountMapStruct;
import plus.xyc.server.main.account.entity.request.CheckResetEmailRequest;
import plus.xyc.server.main.account.entity.request.RegisterRequest;
import plus.xyc.server.main.account.entity.request.SendEmailCodeRequest;
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
@Tag(name = "AccountController", description = "用户账户")
public class AccountController {

    @Resource
    private AccountService accountService;
    @Resource
    private AccountMapStruct accountMapStruct;
    @DubboReference
    private AuthAccountApiService authAccountApiService;

    @PublicRequest
    @PostMapping("/register/email/send")
    @Operation(summary = "发送邮件验证码")
    @Throttler(value = "register.send", limit = 5)
    public void sendRegisterEmail(@RequestBody SendEmailCodeRequest request) {
        this.accountService.sendRegisterEmail(request.getEmail());
    }

    @PublicRequest
    @PostMapping("/register")
    @Operation(summary = "注册")
    public TokenResponse register(@RequestBody RegisterRequest request) {
        return this.accountService.register(request);
    }

    @PublicRequest
    @PostMapping("/reset/email/send")
    @Operation(summary = "发送重置邮件验证码")
    @Throttler(value = "reset.send", limit = 5)
    public void sendResetEmail(@RequestBody SendEmailCodeRequest request) {
        this.accountService.sendResetEmail(request.getEmail());
    }

    @PublicRequest
    @PostMapping("/reset/email/check")
    @Operation(summary = "重置密码验证码校验")
    public TokenResponse checkResetEmail(@RequestBody CheckResetEmailRequest request) {
        return this.accountService.checkResetEmail(request);
    }

    @PostMapping("/reset/password")
    @Operation(summary = "重置密码")
    public TokenResponse resetPassword(
            @CurrentUser() @Parameter(hidden = true) SessionUser user,
            @RequestBody ResetPasswordRequest request
    ) {
        request.setId(user.getId());
        return this.accountService.resetPassword(request);
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

    @PostMapping("/password/change")
    @Operation(summary = "修改密码")
    public void changePassword(
            @CurrentUser() @Parameter(hidden = true) SessionUser user,
            @RequestBody ChangePasswordRequest request,
            @RequestHeader("Authorization") String token
    ) {
        request.setId(user.getId());
        this.accountService.changePassword(request, token.replaceAll("Bearer ", ""));
    }

    @PostMapping("/email/code/send")
    @Operation(summary = "发送邮件验证码")
    @Throttler(value = "email.code.send", limit = 5)
    public void sendEmailCode(
            @CurrentUser() @Parameter(hidden = true) SessionUser user,
            @RequestBody SendEmailCodeRequest request
    ) {
        this.accountService.sendEmailCode(user.getId(), request.getAction());
    }

}
