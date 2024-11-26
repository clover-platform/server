package plus.xyc.server.i18n.module.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.module.entity.request.ModuleUpdateRequest;
import plus.xyc.server.i18n.module.entity.response.ModuleDashboardResponse;
import plus.xyc.server.i18n.module.entity.response.ModuleLanguageResponse;
import plus.xyc.server.i18n.module.entity.response.ModuleResponse;
import plus.xyc.server.i18n.module.service.ModuleService;

import java.util.List;

/**
 * <p>
 * 项目 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/")
@Tag(name = "module", description = "模块")
@Slf4j
public class ModuleController {

    @Resource
    private ModuleService moduleService;

    @GetMapping("/{moduleName}/dashboard")
    @Operation(summary = "概览")
    public ModuleDashboardResponse dashboard(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest
    ) {
        return moduleService.dashboard(pathRequest.getModule().getId());
    }

    @GetMapping("/{moduleName}/languages")
    @Operation(summary = "语言列表")
    public List<ModuleLanguageResponse> languages(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest
    ) {
        return moduleService.languages(pathRequest.getModule().getId());
    }

    @DeleteMapping("/{moduleName}")
    @Operation(summary = "删除模块")
    public void delete(
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest
    ) {
        moduleService.delete(pathRequest.getModule().getId(), user.getId());
    }

    @GetMapping("/{moduleName}")
    @Operation(summary = "模块详情")
    public ModuleResponse detail(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest
    ) {
        return moduleService.detail(pathRequest.getModule().getId());
    }

    @PutMapping("/{moduleName}")
    @Operation(summary = "更新基本信息")
    public void update(
            @RequestBody ModuleUpdateRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest
    ) {
        request.setId(pathRequest.getModule().getId());
        request.setUserId(user.getId());
        moduleService.update(request);
    }

}
