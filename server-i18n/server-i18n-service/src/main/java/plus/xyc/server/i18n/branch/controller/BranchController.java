package plus.xyc.server.i18n.branch.controller;

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
import plus.xyc.server.i18n.branch.entity.dto.Branch;
import plus.xyc.server.i18n.branch.entity.request.*;
import plus.xyc.server.i18n.branch.entity.response.BranchMergeOverviewResponse;
import plus.xyc.server.i18n.branch.entity.response.BranchResponse;
import plus.xyc.server.i18n.branch.service.BranchService;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.annotation.Recount;
import plus.xyc.server.i18n.common.entity.PathRequest;

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
@RequestMapping("/{moduleName}/branch")
@Tag(name = "BranchController", description = "分支")
public class BranchController {

    @Resource
    private BranchService branchService;

    @GetMapping("/all")
    @Operation(summary = "查询所有分支")
    public List<BranchResponse> all(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest,
            @ParameterObject @ModelAttribute BranchAllRequest request
    ) {
        request.setModuleId(pathRequest.getModule().getId());
        return branchService.all(request);
    }

    @GetMapping("/list")
    @Operation(summary = "查询分支")
    public PageResult<Branch> list(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest,
            @ParameterObject @ModelAttribute PageRequest page,
            @ParameterObject @ModelAttribute BranchListRequest request
    ) {
        request.setModuleId(pathRequest.getModule().getId());
        return branchService.list(page, request);
    }

    @Recount
    @PostMapping("/create")
    @Operation(summary = "创建分支")
    public void create(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @RequestBody BranchCreateRequest request,
            @PathInject PathRequest pathRequest,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setModuleId(pathRequest.getModule().getId());
        request.setUserId(user.getId());
        branchService.create(request);
    }

    @PutMapping("/{id}/rename")
    @Operation(summary = "重命名")
    public void rename(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "分支ID") @PathVariable Long id,
            @RequestBody BranchRenameRequest request,
            @PathInject PathRequest pathRequest
    ) {
        request.setId(id);
        request.setModuleId(pathRequest.getModule().getId());
        branchService.rename(request);
    }

    @Recount
    @DeleteMapping("/{id}")
    @Operation(summary = "删除")
    public void delete(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "分支ID") @PathVariable Long id
    ) {
        branchService.delete(id);
    }

    @GetMapping("/{id}/merge/overview")
    @Operation(summary = "合并概览")
    public BranchMergeOverviewResponse mergeOverview(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "分支ID") @PathVariable Long id
    ) {
        return branchService.mergeOverview(id);
    }

    @Recount
    @PutMapping("/{id}/merge")
    @Operation(summary = "合并")
    public void merge(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "分支ID") @PathVariable Long id,
            @RequestBody BranchMergeRequest request,
            @PathInject PathRequest pathRequest
    ) {
        request.setId(id);
        request.setModuleId(pathRequest.getModule().getId());
        branchService.merge(request);
    }

}
