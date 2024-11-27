package plus.xyc.server.main.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.main.account.entity.dto.AccountAccessToken;
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

}
