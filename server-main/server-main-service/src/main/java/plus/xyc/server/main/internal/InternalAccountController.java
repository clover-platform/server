package plus.xyc.server.main.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.main.account.entity.mapstruct.AccountMapStruct;
import plus.xyc.server.main.account.service.AccountService;
import plus.xyc.server.main.api.constant.MainApiRoute;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

import java.util.List;

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
    public ApiAccountResponse getById(@Parameter(description = "ID") @RequestParam("id") Long id) {
        return accountMapStruct.toApiAccountResponse(accountService.getById(id));
    }

    @Operation(summary = "批量查询用户")
    @GetMapping(MainApiRoute.ACCOUNT_LIST)
    public PageResult<ApiAccountResponse> list(
            @Parameter(description = "IDs") @RequestParam("ids") List<Long> ids,
            @Parameter(description = "限制") @RequestParam(value = "size", defaultValue = "100") Integer size,
            @Parameter(description = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return accountService.findByIds(ids, size, page);
    }
}
