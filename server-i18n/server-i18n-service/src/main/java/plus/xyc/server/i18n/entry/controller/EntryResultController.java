package plus.xyc.server.i18n.entry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.annotation.Recount;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryAIResultRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryResultListRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryResultSaveRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryResultResponse;
import plus.xyc.server.i18n.entry.service.EntryResultService;

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
@RequestMapping("/{moduleName}/file/{fileId}/entry/{entryId}/result")
@Tag(name = "EntryResultController", description = "词条翻译结果")
public class EntryResultController {

    @Resource
    private EntryResultService entryResultService;

    @PostMapping("/ai")
    @Operation(summary = "AI建议")
    public List<String> ai(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(description = "词条ID") @PathVariable Long entryId,
            @PathInject PathRequest pathRequest,
            @RequestBody EntryAIResultRequest request
    ) {
        request.setEntryId(entryId);
        return entryResultService.ai(request);
    }

    @Recount
    @PostMapping("/save")
    @Operation(summary = "保存翻译")
    public void save(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody EntryResultSaveRequest request,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(description = "词条ID") @PathVariable Long entryId,
            @PathInject PathRequest pathRequest
    ) {
        request.setModuleId(pathRequest.getModule().getId());
        request.setEntryId(entryId);
        request.setUserId(user.getId());
        entryResultService.saveResult(request);
    }

    @Recount
    @DeleteMapping("/{id}")
    @Operation(summary = "删除翻译")
    public void delete(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @Parameter(description = "翻译结果ID") @PathVariable Long id,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(description = "词条ID") @PathVariable Long entryId,
            @PathInject PathRequest pathRequest
    ) {
        entryResultService.delete(entryId, id, user.getId());
    }

    @GetMapping("/list")
    @Operation(summary = "查询翻译结果")
    public PageResult<EntryResultResponse> list(
            @ParameterObject @ModelAttribute PageRequest page,
            @ParameterObject @ModelAttribute EntryResultListRequest request,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(description = "词条ID") @PathVariable Long entryId,
            @PathInject PathRequest pathRequest
    ) {
        request.setEntryId(entryId);
        return entryResultService.query(page, request);
    }

    @Recount
    @PutMapping("/{id}/approve")
    @Operation(summary = "批准翻译")
    public void approve(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @Parameter(description = "翻译结果ID") @PathVariable Long id,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(description = "词条ID") @PathVariable Long entryId,
            @PathInject PathRequest pathRequest
    ) {
        entryResultService.approve(entryId, id, user.getId());
    }

    @Recount
    @PutMapping("/{id}/remove/approval")
    @Operation(summary = "批准翻译")
    public void removeApproval(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @Parameter(description = "翻译结果ID") @PathVariable Long id,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(description = "词条ID") @PathVariable Long entryId,
            @PathInject PathRequest pathRequest
    ) {
        entryResultService.removeApproval(entryId, id, user.getId());
    }

}
