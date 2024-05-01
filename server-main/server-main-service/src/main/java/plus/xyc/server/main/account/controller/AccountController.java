package plus.xyc.server.main.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import plus.xyc.server.main.account.entity.dto.Account;
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
@RequestMapping("/api/account")
public class AccountController {

    private AccountService accountService;

    @GetMapping("/get")
    public Account findByUsername(@Param("username") String username) {
        return accountService.findByUsername(username);
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
