package plus.xyc.server.i18n.branch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.branch.entity.dto.Branch;
import plus.xyc.server.i18n.branch.entity.request.*;
import plus.xyc.server.i18n.branch.entity.response.BranchMergeOverviewResponse;
import plus.xyc.server.i18n.branch.entity.response.BranchResponse;
import plus.xyc.server.i18n.branch.service.BranchService;

import java.util.List;

/**
 * <p>
 * 分支 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/branch")
@Tag(name = "branch", description = "分支")
public class BranchController {

    @Resource
    private BranchService branchService;

    @GetMapping("/all")
    @Operation(summary = "查询所有分支")
    public List<BranchResponse> all(@ParameterObject @ModelAttribute BranchAllRequest request) {
        return branchService.all(request);
    }

    @GetMapping("/list")
    @Operation(summary = "查询分支")
    public PageResult<Branch> list(
            @ParameterObject @ModelAttribute PageQueryRequest page,
            @ParameterObject @ModelAttribute BranchListRequest request
    ) {
        return branchService.list(page, request);
    }

    @PostMapping("/create")
    @Operation(summary = "创建分支")
    public void create(@RequestBody BranchCreateRequest request) {
        branchService.create(request);
    }

    @PutMapping("/{id}/rename")
    @Operation(summary = "重命名")
    public void rename(
            @Parameter(description = "分支ID") @PathVariable Long id,
            @RequestBody BranchRenameRequest request
    ) {
        request.setId(id);
        branchService.rename(request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除")
    public void delete(@Parameter(description = "分支ID") @PathVariable Long id) {
        branchService.delete(id);
    }

    @GetMapping("/{id}/merge/overview")
    @Operation(summary = "合并概览")
    public BranchMergeOverviewResponse mergeOverview(@Parameter(description = "分支ID") @PathVariable Long id) {
        return branchService.mergeOverview(id);
    }

    @PutMapping("/{id}/merge")
    @Operation(summary = "合并")
    public void merge(
            @Parameter(description = "分支ID") @PathVariable Long id,
            @RequestBody BranchMergeRequest request
    ) {
        request.setId(id);
        branchService.merge(request);
    }

}
