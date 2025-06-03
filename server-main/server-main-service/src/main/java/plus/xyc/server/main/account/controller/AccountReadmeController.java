package plus.xyc.server.main.account.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import plus.xyc.server.main.account.entity.request.UpdateReadmeRequest;
import plus.xyc.server.main.account.service.AccountReadmeService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generator
 * @since 2025-06-03
 */
@RestController
@RequestMapping("/account/readme")
@Tag(name = "AccountReadmeController", description = "关于我")
public class AccountReadmeController {

    @Resource
    private AccountReadmeService accountReadmeService;

    @PutMapping("/update")
    @Operation(summary = "更新")
    public void update(
            @CurrentUser() @Parameter(hidden = true) SessionUser user,
            @RequestBody UpdateReadmeRequest request
    ) {
        request.setAccountId(user.getId());
       accountReadmeService.update(request);
    }

}
