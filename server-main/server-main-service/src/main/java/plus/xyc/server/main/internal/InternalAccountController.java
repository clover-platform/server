package plus.xyc.server.main.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import plus.xyc.server.main.account.entity.mapstruct.AccountMapStruct;
import plus.xyc.server.main.account.service.AccountService;
import plus.xyc.server.main.api.constant.MainApiRoute;
import plus.xyc.server.main.api.entity.response.AccountResponse;

@RestController
@Slf4j
@Tag(name = "[inner]account", description = "[内部接口]用户账户")
public class InternalAccountController {

    @Resource
    private AccountService accountService;
    @Resource
    private AccountMapStruct accountMapStruct;

    @Operation(summary = "根据ID查找账户")
    @GetMapping(MainApiRoute.ACCOUNT_GET_BY_ID)
    public AccountResponse getById(@Parameter(description = "ID") @RequestParam("id") Long id) {
        return accountMapStruct.toAccountResponse(accountService.getById(id));
    }
}
