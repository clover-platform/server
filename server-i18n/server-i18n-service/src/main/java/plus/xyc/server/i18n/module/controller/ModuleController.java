package plus.xyc.server.i18n.module.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.boot.auth.annotation.CurrentUser;
import org.zkit.support.starter.boot.entity.SessionUser;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.module.entity.request.CreateModuleRequest;
import plus.xyc.server.i18n.module.entity.request.ModuleQueryRequest;
import plus.xyc.server.i18n.module.entity.response.ModuleDashboardResponse;
import plus.xyc.server.i18n.module.entity.response.ModuleLanguageResponse;
import plus.xyc.server.i18n.module.entity.response.ModuleResponse;
import plus.xyc.server.i18n.module.service.ModuleService;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.rest.MainAccountRestApi;

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
@RequestMapping("/module")
@Tag(name = "module", description = "模块")
@Slf4j
public class ModuleController {

    @Resource
    private ModuleService moduleService;
    @Resource
    private MainAccountRestApi accountRestApi;

    @GetMapping("/list")
    @Operation(summary = "列表")
    public PageResult<ModuleResponse> list(
            @ModelAttribute PageQueryRequest page,
            @ModelAttribute ModuleQueryRequest query,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        query.setUserId(user.getId());
        return moduleService.query(page, query);
    }

    @PostMapping("/new")
    @Operation(summary = "创建")
    public void newModule(
            @RequestBody CreateModuleRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setOwner(user.getId());
        ApiAccountResponse account = accountRestApi.getById(user.getId()).getData();
        request.setProjectId(account.getCurrentProjectId());
        moduleService.create(request);
    }

    @GetMapping("/{id}/dashboard")
    @Operation(summary = "概览")
    public ModuleDashboardResponse dashboard(@Parameter(description = "模块ID") @PathVariable Long id) {
        return moduleService.dashboard(id);
    }

    @GetMapping("/{id}/languages")
    @Operation(summary = "语言列表")
    public List<ModuleLanguageResponse> languages(@Parameter(description = "模块ID") @PathVariable Long id) {
        return moduleService.languages(id);
    }

}
