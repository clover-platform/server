package plus.xyc.server.main.account.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.server.account.api.entity.AccountResponse;
import org.zkit.support.server.account.api.rest.AuthAccountApi;
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

    @GetMapping("/test")
    public AccountResponse test(@RequestParam("username") String username) {
        AccountResponse response = authAccountApi.findByUsername(username);
        log.info("AccountController test {}", response);
        return response;
    }

    @PostMapping("/register/email/send")
    public void sendRegisterEmail(@RequestBody SendRegisterEmailRequest request) {
        this.accountService.sendRegisterEmail(request.getEmail());
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
