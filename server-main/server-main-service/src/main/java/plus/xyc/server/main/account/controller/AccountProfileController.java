package plus.xyc.server.main.account.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.starter.security.annotation.PublicRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import plus.xyc.server.main.account.entity.response.AccountProfileResponse;
import plus.xyc.server.main.account.service.AccountService;

@RestController
@RequestMapping("/account/profile")
@Tag(name = "AccountProfileController", description = "用户资料")
public class AccountProfileController {

    @Resource
    private AccountService accountService;

    @PublicRequest
    @GetMapping("/{username}")
    @Operation(summary = "用户资料")
    public AccountProfileResponse profile(@PathVariable String username) {
        return accountService.profile(username);
    }

}
