package plus.xyc.server.main.account.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.cloud.starter.auth.annotation.PublicRequest;
import org.zkit.support.cloud.starter.entity.Result;
import org.zkit.support.server.account.api.entity.response.AccountResponse;
import org.zkit.support.server.account.api.entity.response.OTPResponse;
import org.zkit.support.server.account.api.entity.response.TokenResponse;
import org.zkit.support.server.account.api.rest.AuthAccountApi;
import org.zkit.support.throttler.starter.annotation.Throttler;
import plus.xyc.server.main.account.entity.request.CheckRegisterEmailRequest;
import plus.xyc.server.main.account.entity.request.SendRegisterEmailRequest;
import plus.xyc.server.main.account.service.AccountService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-02
 */
@RestController
@RequestMapping("/api/main/account")
@Slf4j
public class AccountController {

    private AccountService accountService;
    private AuthAccountApi authAccountApi;

    @PublicRequest
    @GetMapping("/test")
    public AccountResponse test(@RequestParam("username") String username) {
        Result<AccountResponse> response = authAccountApi.findByUsername(username);
        log.info("AccountController test {}", response);
        return response.getData();
    }

    @PublicRequest
    @PostMapping("/register/email/send")
    public void sendRegisterEmail(@RequestBody SendRegisterEmailRequest request) {
        this.accountService.sendRegisterEmail(request.getEmail());
    }

    @PublicRequest
    @PostMapping("/register/email/check")
    @Throttler(value = "register", limit = 5)
    public TokenResponse checkRegisterEmail(@RequestBody CheckRegisterEmailRequest request) {
        return this.accountService.checkRegisterEmail(request);
    }

    @PublicRequest
    @GetMapping("/otp/secret")
    public OTPResponse otpSecret() {
        Result<OTPResponse> response = authAccountApi.otpSecret(1L);
        return response.getData();
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
