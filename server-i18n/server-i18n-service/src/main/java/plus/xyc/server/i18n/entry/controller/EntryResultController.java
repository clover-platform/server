package plus.xyc.server.i18n.entry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.entry.entity.request.EntryAIResultRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryResultListRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryResultSaveRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryResultResponse;
import plus.xyc.server.i18n.entry.service.EntryResultService;
import plus.xyc.server.i18n.module.annotation.ModuleInject;
import plus.xyc.server.i18n.module.entity.dto.Module;

import java.util.List;

/**
 * <p>
 * 翻译结果 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/{module}/entry/{entryId}/result")
public class EntryResultController {

    @Resource
    private EntryResultService entryResultService;

    @PostMapping("/ai")
    @Operation(summary = "AI建议")
    public List<String> ai(
            @Parameter(description = "模块标识") @PathVariable("module") String identifier,
            @Parameter(description = "词条ID") @PathVariable("entryId") Long entryId,
            @RequestBody EntryAIResultRequest request
    ) {
        request.setEntryId(entryId);
        return entryResultService.ai(request);
    }

    @PostMapping("/save")
    @Operation(summary = "保存翻译")
    public void save(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody EntryResultSaveRequest request,
            @Parameter(description = "模块标识") @PathVariable("module") String identifier,
            @Parameter(description = "词条ID") @PathVariable("entryId") Long entryId,
            @ModuleInject Module module
    ) {
        request.setModuleId(module.getId());
        request.setEntryId(entryId);
        request.setUserId(user.getId());
        entryResultService.saveResult(request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除翻译")
    public void delete(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @Parameter(description = "翻译结果ID") @PathVariable Long id,
            @Parameter(description = "模块标识") @PathVariable("module") String identifier,
            @Parameter(description = "词条ID") @PathVariable("entryId") Long entryId
    ) {
        entryResultService.delete(id, user.getId());
    }

    @GetMapping("/list")
    @Operation(summary = "查询翻译结果")
    public PageResult<EntryResultResponse> list(
            @ParameterObject @ModelAttribute PageQueryRequest page,
            @ParameterObject @ModelAttribute EntryResultListRequest request,
            @Parameter(description = "模块标识") @PathVariable("module") String identifier,
            @Parameter(description = "词条ID") @PathVariable("entryId") Long entryId
    ) {
        request.setEntryId(entryId);
        return entryResultService.query(page, request);
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "批准翻译")
    public void approve(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @Parameter(description = "翻译结果ID") @PathVariable Long id,
            @Parameter(description = "模块标识") @PathVariable("module") String identifier,
            @Parameter(description = "词条ID") @PathVariable("entryId") Long entryId
    ) {
        entryResultService.approve(id, user.getId());
    }

    @PutMapping("/{id}/remove/approval")
    @Operation(summary = "批准翻译")
    public void removeApproval(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @Parameter(description = "翻译结果ID") @PathVariable Long id,
            @Parameter(description = "模块标识") @PathVariable("module") String identifier,
            @Parameter(description = "词条ID") @PathVariable("entryId") Long entryId
    ) {
        entryResultService.removeApproval(id, user.getId());
    }

}
