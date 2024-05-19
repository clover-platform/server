package plus.xyc.server.i18n.branch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import plus.xyc.server.i18n.branch.entity.dto.Branch;
import plus.xyc.server.i18n.branch.entity.request.AllBranchRequest;
import plus.xyc.server.i18n.branch.entity.response.BranchResponse;
import plus.xyc.server.i18n.branch.service.BranchService;
import plus.xyc.server.i18n.module.entity.response.ModuleLanguageResponse;

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
    public List<BranchResponse> all(@ModelAttribute AllBranchRequest request) {
        return branchService.all(request);
    }

}
