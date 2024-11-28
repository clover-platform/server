package plus.xyc.server.main.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.main.account.entity.dto.AccountAccessToken;
import plus.xyc.server.main.account.entity.request.AccountAccessTokenCreateRequest;
import plus.xyc.server.main.account.entity.request.AccountAccessTokenRevokeRequest;
import plus.xyc.server.main.account.service.AccountAccessTokenService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-11-27
 */
@RestController
@RequestMapping("/account/access/token")
@Tag(name = "accountAccessToken", description = "访问令牌")
@Slf4j
public class AccountAccessTokenController {

    @Resource
    private AccountAccessTokenService accountAccessTokenService;

    @GetMapping("/list")
    @Operation(summary = "列表")
    public PageResult<AccountAccessToken> list(
            @ParameterObject @ModelAttribute PageRequest page,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        return accountAccessTokenService.list(page, user.getId());
    }

    @PostMapping("/create")
    @Operation(summary = "创建新令牌")
    public String create(
            @RequestBody AccountAccessTokenCreateRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setAccountId(user.getId());
        return accountAccessTokenService.create(request);
    }

    @DeleteMapping("/{id}/revoke")
    @Operation(summary = "撤销令牌")
    public void revoke(
            @RequestBody AccountAccessTokenRevokeRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @Parameter(description = "令牌 ID") @PathVariable Long id
    ) {
        request.setAccountId(user.getId());
        request.setTokenId(id);
        accountAccessTokenService.revoke(request);
    }

}
