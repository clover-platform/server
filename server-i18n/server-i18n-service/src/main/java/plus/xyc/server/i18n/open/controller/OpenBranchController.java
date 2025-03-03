package plus.xyc.server.i18n.open.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.branch.service.BranchService;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.open.annotation.OpenUser;
import plus.xyc.server.i18n.open.entity.request.OpenBranchCreateRequest;

@RestController
@RequestMapping("/open/{moduleName}/branch")
@Tag(name = "OpenBranchController", description = "分支接口")
@Slf4j
public class OpenBranchController {

    @Resource
    private BranchService branchService;

    @PostMapping("/create/if/not/exist")
    @Operation(summary = "创建")
    public void create(
            @RequestBody OpenBranchCreateRequest request,
            @OpenUser @Parameter(hidden = true) SessionUser user,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest
    ) {
        request.setModuleId(pathRequest.getModule().getId());
        request.setUserId(user.getId());
        branchService.createIfNotExist(request);
    }

}
