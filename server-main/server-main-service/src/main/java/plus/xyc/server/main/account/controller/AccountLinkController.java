package plus.xyc.server.main.account.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.server.account.api.entity.request.AuthLinkBindRequest;
import org.zkit.support.server.account.api.entity.response.TokenResponse;
import org.zkit.support.starter.security.annotation.PublicRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.main.account.service.AccountLinkService;

@RestController
@RequestMapping("/account/link")
@Slf4j
@Validated
@Tag(name = "AccountLinkController", description = "用户账户链接")
public class AccountLinkController {

    @Resource
    private AccountLinkService accountLinkService;

    @PublicRequest
    @Operation(summary = "验证临时授权")
    @PostMapping("/bind")
    public TokenResponse bind(@RequestBody AuthLinkBindRequest request) {
        return accountLinkService.bind(request);
    }
}