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
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.annotation.Recount;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCreateRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryEditRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCountResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithStateResponse;
import plus.xyc.server.i18n.entry.service.EntryService;

/**
 * <p>
 * 词条 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/{moduleName}/branch/{branchName}/entry")
@Tag(name = "entry", description = "词条")
public class EntryController {

    @Resource
    private EntryService entryService;

    @Recount
    @PostMapping("/create")
    @Operation(summary = "创建词条")
    public void create(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody EntryCreateRequest request,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "分支ID") @PathVariable String branchName,
            @PathInject PathRequest pathRequest
    ) {
        request.setModuleId(pathRequest.getModule().getId());
        request.setUserId(user.getId());
        entryService.create(request);
    }

    @GetMapping("/{entryId}")
    @Operation(summary = "词条详情")
    public EntryWithStateResponse detail(
            @Parameter(description = "语言") @RequestParam String language,
            @Parameter(description = "词条ID") @PathVariable("entryId") Long id,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "分支ID") @PathVariable String branchName,
            @PathInject PathRequest pathRequest
    ) {
        return entryService.detail(id, language);
    }

    @Recount
    @DeleteMapping("/{entryId}")
    @Operation(summary = "删除词条")
    public void delete(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @Parameter(description = "词条ID") @PathVariable("entryId") Long id,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "分支ID") @PathVariable String branchName,
            @PathInject PathRequest pathRequest
    ) {
        entryService.remove(id, user.getId());
    }

    @PutMapping("/{entryId}")
    @Operation(summary = "更新词条")
    public void edit(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody EntryEditRequest request,
            @Parameter(description = "词条ID") @PathVariable("entryId") Long id,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "分支ID") @PathVariable String branchName,
            @PathInject PathRequest pathRequest
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
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "分支ID") @PathVariable String branchName,
            @PathInject PathRequest pathRequest
    ) {
        request.setModuleId(pathRequest.getModule().getId());
        return entryService.query(page, request);
    }

    @GetMapping("/count")
    @Operation(summary = "统计词条")
    public EntryCountResponse count(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "分支ID") @PathVariable String branchName,
            @PathInject PathRequest pathRequest,
            @ParameterObject @ModelAttribute EntryCountRequest request
    ) {
        request.setModuleId(pathRequest.getModule().getId());
        return entryService.count(request);
    }

}
