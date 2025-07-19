package plus.xyc.server.i18n.module.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.server.message.api.aspect.annotation.Activity;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;

import plus.xyc.server.i18n.module.entity.dto.Module;
import plus.xyc.server.i18n.activity.entity.ActivityAction;
import plus.xyc.server.i18n.module.entity.request.ModuleAllRequest;
import plus.xyc.server.i18n.module.entity.request.ModuleCreateRequest;
import plus.xyc.server.i18n.module.entity.request.ModuleQueryRequest;
import plus.xyc.server.i18n.module.entity.response.ModuleResponse;
import plus.xyc.server.i18n.module.service.ModuleCollectService;
import plus.xyc.server.i18n.module.service.ModuleService;
import plus.xyc.server.main.api.aspect.annotation.AccountHolderLoader;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.holder.AccountHolder;

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
@Tag(name = "ModuleCommonController", description = "模块")
@Slf4j
public class ModuleCommonController {

    @Resource
    private ModuleService moduleService;
    @Resource
    private ModuleCollectService moduleCollectService;

    @GetMapping("/list")
    @Operation(summary = "列表")
    public PageResult<ModuleResponse> list(
            @ParameterObject @ModelAttribute PageRequest page,
            @ParameterObject @ModelAttribute ModuleQueryRequest query,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        query.setUserId(user.getId());
        return moduleService.query(page, query);
    }

    @GetMapping("/collect/my")
    @Operation(summary = "我的收藏")
    public List<Module> my(@CurrentUser() @Parameter(hidden = true) SessionUser user) {
        return moduleCollectService.my(user.getId());
    }

    @PostMapping("/new")
    @Operation(summary = "创建")
    @Activity(action = ActivityAction.CREATE_MODULE, userId = "#user.id", title = "#request.name", url = "#request.identifier + '/dashboard'")
    @AccountHolderLoader
    public void newModule(
            @RequestBody ModuleCreateRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        ApiAccountResponse account = AccountHolder.get();
        request.setProjectId(account.getCurrentProjectId());
        request.setOwner(user.getId());
        moduleService.create(request);
    }

    @GetMapping("/all")
    @Operation(summary = "我的全部模块")
    public List<ModuleResponse> all(
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @ParameterObject @ModelAttribute ModuleAllRequest request
    ) {
        request.setUserId(user.getId());
        return moduleService.all(request);
    }

}
