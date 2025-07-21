package plus.xyc.server.i18n.module.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.module.service.ModuleCollectService;
import plus.xyc.server.main.api.annotation.CurrentAccount;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generator
 * @since 2025-06-30
 */
@RestController
@RequestMapping("/{moduleName}/collect")
@Tag(name = "ModuleCollectController", description = "收藏")
public class ModuleCollectController {

    @Resource
    private ModuleCollectService moduleCollectService;

    @PostMapping("/add")
    @Operation(summary = "添加收藏")
    public void add(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest,
            @CurrentAccount @Parameter(hidden = true) ApiAccountResponse account
    ) {
       moduleCollectService.add(account.getId(), account.getCurrentProjectId(), pathRequest.getModule().getId());
    }

    @PostMapping("/cancel")
    @Operation(summary = "取消收藏")
    public void cancel(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest,
            @CurrentAccount @Parameter(hidden = true) ApiAccountResponse account
    ) {
        moduleCollectService.cancel(account.getId(), account.getCurrentProjectId(), pathRequest.getModule().getId());
    }

}
