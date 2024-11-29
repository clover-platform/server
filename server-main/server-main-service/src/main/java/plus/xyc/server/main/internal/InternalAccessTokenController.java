package plus.xyc.server.main.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.main.account.service.AccountAccessTokenService;
import plus.xyc.server.main.api.constant.MainApiRoute;

@RestController
@Slf4j
@Tag(name = "internal-access-token", description = "[内部接口]访问令牌")
public class InternalAccessTokenController {

    @Resource
    private AccountAccessTokenService accountAccessTokenService;

    @Operation(summary = "校验Token，并返回用户信息")
    @GetMapping(MainApiRoute.CHECK_ACCESS_TOKEN)
    public SessionUser checkAccessToken(@Parameter(description = "ID") @RequestParam("token") String token) {
        return accountAccessTokenService.checkAccessToken(token);
    }

}
