package plus.xyc.server.i18n.open.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.annotation.Recount;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.entry.service.EntryService;
import plus.xyc.server.i18n.open.annotation.OpenUser;
import plus.xyc.server.i18n.open.entity.request.OpenEntryPullRequest;
import plus.xyc.server.i18n.open.entity.request.OpenEntryPushRequest;

@RestController
@RequestMapping("/open/{moduleName}/file/{fileName}/entry")
@Tag(name = "OpenEntryController", description = "词条接口")
@Slf4j
public class OpenEntryController {

    @Resource
    private EntryService entryService;

    @Recount
    @PostMapping("/push")
    @Operation(summary = "推送词条")
    public void push(
            @RequestBody JSONObject content,
            @OpenUser @Parameter(hidden = true) SessionUser user,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件名") @PathVariable String fileName,
            @PathInject PathRequest pathRequest
    ) {
        OpenEntryPushRequest request = new OpenEntryPushRequest();
        request.setModuleId(pathRequest.getModule().getId());
        request.setBranchId(pathRequest.getFile().getId());
        request.setContent(content);
        request.setUserId(user.getId());
        entryService.push(request);
    }

    @GetMapping("/pull")
    @Operation(summary = "获取翻译")
    public JSONObject pull(
            @ParameterObject @ModelAttribute OpenEntryPullRequest request,
            @OpenUser @Parameter(hidden = true) SessionUser user,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件名") @PathVariable String fileName,
            @PathInject PathRequest pathRequest
    ) {
        request.setModuleId(pathRequest.getModule().getId());
        request.setBranchId(pathRequest.getFile().getId());
        request.setUserId(user.getId());
        return entryService.pull(request);
    }

}
