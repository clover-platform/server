package plus.xyc.server.i18n.entry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.boot.auth.annotation.CurrentUser;
import org.zkit.support.starter.boot.entity.SessionUser;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
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
@RequestMapping("/entry")
@Tag(name = "entry", description = "词条")
public class EntryController {

    @Resource
    private EntryService entryService;

    @PostMapping("/create")
    @Operation(summary = "创建词条")
    public void create(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody EntryCreateRequest request
    ) {
        request.setUserId(user.getId());
        entryService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "词条详情")
    public EntryWithStateResponse detail(
            @Parameter(description = "语言") @RequestParam String language,
            @Parameter(description = "词条ID") @PathVariable Long id) {
        return entryService.detail(id, language);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除词条")
    public void delete(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @Parameter(description = "词条ID") @PathVariable Long id
    ) {
        entryService.remove(id, user.getId());
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新词条")
    public void edit(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody EntryEditRequest request,
            @Parameter(description = "词条ID") @PathVariable Long id
    ) {
        request.setId(id);
        request.setUserId(user.getId());
        entryService.edit(request);
    }

    @GetMapping("/list")
    @Operation(summary = "查询词条")
    public PageResult<EntryWithStateResponse> list(
            @ParameterObject @ModelAttribute PageQueryRequest page,
            @ParameterObject @ModelAttribute EntryListRequest request
    ) {
        return entryService.query(page, request);
    }

    @GetMapping("/count")
    @Operation(summary = "查询词条")
    public EntryCountResponse count(@ParameterObject @ModelAttribute EntryCountRequest request) {
        return entryService.count(request);
    }

}
