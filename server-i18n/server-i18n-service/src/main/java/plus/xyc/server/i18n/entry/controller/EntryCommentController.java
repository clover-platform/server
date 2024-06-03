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
@RequestMapping("/entry/comment")
public class EntryCommentController {

    @Resource
    private EntryCommentService entryCommentService;

    @GetMapping("/list")
    @Operation(summary = "查询评论")
    public PageResult<EntryCommentResponse> list(
            @ParameterObject @ModelAttribute PageQueryRequest page,
            @ParameterObject @ModelAttribute EntryCommentListRequest request
    ) {
        return entryCommentService.query(page, request);
    }

    @PostMapping("/add")
    @Operation(summary = "添加评论")
    public void add(
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @RequestBody EntryCommentAddRequest request
    ) {
        request.setCreateUserId(user.getId());
        entryCommentService.add(request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "添加评论")
    public void delete(
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @Parameter(description = "评论ID") @PathVariable Long id
    ) {
        entryCommentService.delete(user.getId(), id);
    }

}
