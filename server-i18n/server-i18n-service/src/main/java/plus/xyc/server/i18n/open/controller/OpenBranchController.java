package plus.xyc.server.i18n.open.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.open.annotation.OpenUser;
import plus.xyc.server.i18n.open.entity.request.OpenBranchCreateRequest;

@RestController
@RequestMapping("/open/{moduleName}/branch")
@Tag(name = "Branch Open API", description = "下载包")
@Slf4j
public class OpenBranchController {

    @PostMapping("/create")
    @Operation(summary = "创建")
    public void create(
            @RequestBody OpenBranchCreateRequest request,
            @OpenUser @Parameter(hidden = true) SessionUser user,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest
    ) {
        log.info("create branch {}", request);
        log.info("user {}", user);
        log.info("moduleName {}", moduleName);
        log.info("pathRequest {}", pathRequest);
    }

}
