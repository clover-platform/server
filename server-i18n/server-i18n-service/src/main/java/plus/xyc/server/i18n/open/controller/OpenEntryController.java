package plus.xyc.server.i18n.open.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.annotation.Recount;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.entry.service.EntryService;
import plus.xyc.server.i18n.open.annotation.OpenUser;
import plus.xyc.server.i18n.open.entity.request.OpenEntryPushRequest;

@RestController
@RequestMapping("/open/{moduleName}/branch/{branchName}/entry")
@Tag(name = "Branch Open API", description = "下载包")
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
            @Parameter(description = "分支名称") @PathVariable String branchName,
            @PathInject PathRequest pathRequest
    ) {
        OpenEntryPushRequest request = new OpenEntryPushRequest();
        request.setModuleId(pathRequest.getModule().getId());
        request.setBranchId(pathRequest.getBranch().getId());
        request.setContent(content);
        request.setUserId(user.getId());
        entryService.push(request);
    }

}
