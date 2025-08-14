package plus.xyc.server.i18n.file.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.annotation.Recount;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCreateRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCountResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithStateResponse;
import plus.xyc.server.i18n.entry.service.EntryService;
import plus.xyc.server.i18n.file.entity.request.FileEntryBatchRequest;
import plus.xyc.server.i18n.file.entity.request.FileEntrySearchRequest;

@RestController
@RequestMapping("/{moduleName}/file/entry")
@Tag(name = "FileEntryController", description = "词条")
@Slf4j
public class FileEntryController {

    @Resource
    private EntryService entryService;

    @GetMapping("/list")
    @Operation(summary = "查询所有词条")
    public PageResult<EntryResponse> list(
            @ParameterObject @ModelAttribute FileEntrySearchRequest request,
            @ParameterObject @ModelAttribute PageRequest page,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(hidden = true) @PathInject PathRequest pathRequest) {
        request.setModuleId(pathRequest.getModule().getId());
        return entryService.list(page, request);
    }

    @GetMapping("/all")
    @Operation(summary = "查询所有词条")
    public PageResult<EntryWithStateResponse> entryAll(
            @ParameterObject @ModelAttribute EntryListRequest request,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(hidden = true) @PathInject PathRequest pathRequest) {
        request.setModuleId(pathRequest.getModule().getId());
        return entryService.all(request);
    }

    @GetMapping("/count")
    @Operation(summary = "统计词条")
    public EntryCountResponse entryCount(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest,
            @ParameterObject @ModelAttribute EntryCountRequest request) {
        request.setModuleId(pathRequest.getModule().getId());
        return entryService.count(request);
    }

    @Recount
    @PostMapping("/create")
    @Operation(summary = "创建词条")
    public void create(
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody EntryCreateRequest request,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest) {
        request.setModuleId(pathRequest.getModule().getId());
        request.setUserId(user.getId());
        entryService.create(request);
    }

    @Recount
    @PostMapping("/batch/add")
    @Operation(summary = "批量添加词条")
    public void batchAddEntry(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(hidden = true) @PathInject PathRequest pathRequest,
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody FileEntryBatchRequest request) {
        request.setModuleId(pathRequest.getModule().getId());
        request.setUserId(user.getId());
        entryService.batchAddEntry(request);
    }
    
}
