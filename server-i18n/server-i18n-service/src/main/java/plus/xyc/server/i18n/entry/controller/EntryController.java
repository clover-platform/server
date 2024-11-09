package plus.xyc.server.i18n.entry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCreateRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryEditRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCountResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithStateResponse;
import plus.xyc.server.i18n.entry.service.EntryService;
import plus.xyc.server.i18n.module.annotation.ModuleInject;
import plus.xyc.server.i18n.module.entity.dto.Module;

/**
 * <p>
 * 词条 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/{module}/entry")
@Tag(name = "entry", description = "词条")
public class EntryController {

    @Resource
    private EntryService entryService;

    @PostMapping("/create")
    @Operation(summary = "创建词条")
    public void create(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody EntryCreateRequest request,
            @Parameter(description = "模块标识") @PathVariable("module") String identifier,
            @ModuleInject Module module
    ) {
        request.setModuleId(module.getId());
        request.setUserId(user.getId());
        entryService.create(request);
    }

    @GetMapping("/{entryId}")
    @Operation(summary = "词条详情")
    public EntryWithStateResponse detail(
            @Parameter(description = "语言") @RequestParam String language,
            @Parameter(description = "词条ID") @PathVariable("entryId") Long id,
            @Parameter(description = "模块标识") @PathVariable("module") String identifier,
            @ModuleInject Module module
    ) {
        return entryService.detail(id, language);
    }

    @DeleteMapping("/{entryId}")
    @Operation(summary = "删除词条")
    public void delete(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @Parameter(description = "词条ID") @PathVariable("entryId") Long id,
            @Parameter(description = "模块标识") @PathVariable("module") String identifier,
            @ModuleInject Module module
    ) {
        entryService.remove(id, user.getId());
    }

    @PutMapping("/{entryId}")
    @Operation(summary = "更新词条")
    public void edit(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody EntryEditRequest request,
            @Parameter(description = "词条ID") @PathVariable("entryId") Long id,
            @Parameter(description = "模块标识") @PathVariable("module") String identifier,
            @ModuleInject Module module
    ) {
        request.setId(id);
        request.setUserId(user.getId());
        entryService.edit(request);
    }

    @GetMapping("/list")
    @Operation(summary = "查询词条")
    public PageResult<EntryWithStateResponse> list(
            @ParameterObject @ModelAttribute PageQueryRequest page,
            @ParameterObject @ModelAttribute EntryListRequest request,
            @Parameter(description = "模块标识") @PathVariable("module") String identifier,
            @ModuleInject Module module
    ) {
        request.setModuleId(module.getId());
        return entryService.query(page, request);
    }

    @GetMapping("/count")
    @Operation(summary = "统计词条")
    public EntryCountResponse count(
            @Parameter(description = "模块标识") @PathVariable("module") String identifier,
            @ModuleInject Module module,
            @ParameterObject @ModelAttribute EntryCountRequest request
    ) {
        request.setModuleId(module.getId());
        return entryService.count(request);
    }

}
