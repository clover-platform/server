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
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCommentAddRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCommentListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCommentResponse;
import plus.xyc.server.i18n.entry.service.EntryCommentService;

/**
 * <p>
 * 词条评论 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/{moduleName}/file/{fileId}/entry/{entryId}/comment")
@Tag(name = "EntryCommentController", description = "词条评论")
public class EntryCommentController {

    @Resource
    private EntryCommentService entryCommentService;

    @GetMapping("/list")
    @Operation(summary = "查询评论")
    public PageResult<EntryCommentResponse> list(
            @ParameterObject @ModelAttribute PageRequest page,
            @ParameterObject @ModelAttribute EntryCommentListRequest request,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(description = "词条ID") @PathVariable Long entryId,
            @PathInject PathRequest pathRequest
    ) {
        request.setEntryId(entryId);
        return entryCommentService.query(page, request);
    }

    @PostMapping("/add")
    @Operation(summary = "添加评论")
    public void add(
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @RequestBody EntryCommentAddRequest request,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(description = "词条ID") @PathVariable Long entryId,
            @PathInject PathRequest pathRequest
    ) {
        request.setEntryId(entryId);
        request.setCreateUserId(user.getId());
        entryCommentService.add(request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "添加评论")
    public void delete(
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @Parameter(description = "评论ID") @PathVariable Long id,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(description = "词条ID") @PathVariable Long entryId,
            @PathInject PathRequest pathRequest
    ) {
        entryCommentService.delete(user.getId(), id);
    }

}
