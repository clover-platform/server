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
@RequestMapping("/entry/result")
public class EntryResultController {

    @Resource
    private EntryResultService entryResultService;

    @PostMapping("/ai")
    @Operation(summary = "AI建议")
    public List<String> ai(@RequestBody EntryAIResultRequest request) {
        return entryResultService.ai(request);
    }

    @PostMapping("/save")
    @Operation(summary = "保存翻译")
    public void save(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody EntryResultSaveRequest request
    ) {
        request.setUserId(user.getId());
        entryResultService.saveResult(request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除翻译")
    public void delete(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @Parameter(description = "翻译结果ID") @PathVariable Long id
    ) {
        entryResultService.delete(id, user.getId());
    }

    @GetMapping("/list")
    @Operation(summary = "查询翻译结果")
    public PageResult<EntryResultResponse> list(
            @ParameterObject @ModelAttribute PageQueryRequest page,
            @ParameterObject @ModelAttribute EntryResultListRequest request
    ) {
        return entryResultService.query(page, request);
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "批准翻译")
    public void approve(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @Parameter(description = "翻译结果ID") @PathVariable Long id
    ) {
        entryResultService.approve(id, user.getId());
    }

}
