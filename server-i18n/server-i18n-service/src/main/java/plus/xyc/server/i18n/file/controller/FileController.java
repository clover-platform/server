package plus.xyc.server.i18n.file.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import plus.xyc.server.i18n.file.service.FileService; 
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.annotation.Recount;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.file.entity.request.FileImportRequest;
import plus.xyc.server.i18n.file.entity.request.FileListRequest;
import plus.xyc.server.i18n.file.entity.request.FileRenameRequest;
import plus.xyc.server.i18n.file.entity.request.FileUploadRequest;
import plus.xyc.server.i18n.file.entity.response.FileResponse;
import plus.xyc.server.i18n.file.entity.response.FileUploadResponse;

/**
 * <p>
 * 分支 前端控制器
 * </p>
 *
 * @author generator
 * @since 2025-06-17
 */
@RestController
@RequestMapping("/{moduleName}/file")
@Tag(name = "FileController", description = "文件")
@Slf4j
public class FileController {

    @Resource
    private FileService fileService;

    @GetMapping("/list")
    @Operation(summary = "查询文件")
    public PageResult<FileResponse> list(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(hidden = true) @PathInject PathRequest pathRequest,
            @ParameterObject @ModelAttribute PageRequest page,
            @ParameterObject @ModelAttribute FileListRequest request) {
        request.setModuleId(pathRequest.getModule().getId());
        return fileService.list(page, request);
    }

    @GetMapping("/all")
    @Operation(summary = "查询所有文件")
    public List<FileResponse> all(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(hidden = true) @PathInject PathRequest pathRequest) {
        return fileService.all(pathRequest.getModule().getId());
    }

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public FileUploadResponse upload(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(hidden = true) @PathInject PathRequest pathRequest,
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody FileUploadRequest request) {
        request.setModuleId(pathRequest.getModule().getId());
        request.setUserId(user.getId());
        return fileService.upload(request);
    }

    @PostMapping("/update")
    @Operation(summary = "批量更新文件")
    public void updateBatch(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(hidden = true) @PathInject PathRequest pathRequest,
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody FileUploadRequest request) {
        request.setModuleId(pathRequest.getModule().getId());
        request.setUserId(user.getId());
        fileService.updateBatch(request);
    }

    @Recount
    @DeleteMapping("/{fileId}")
    @Operation(summary = "删除文件")
    public void delete(@Parameter(description = "文件ID") @PathVariable Long fileId) {
        fileService.delete(fileId);
    }

    @GetMapping("/{fileId}/preview")
    @Operation(summary = "预览文件")
    public List<List<String>> preview(@Parameter(description = "文件ID") @PathVariable Long fileId) {
        return fileService.preview(fileId);
    }

    @PostMapping("/{fileId}/import")
    @Operation(summary = "导入文件")
    @Recount
    public void importFile(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(hidden = true) @PathInject PathRequest pathRequest,
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody FileImportRequest request) {
        request.setModuleId(pathRequest.getModule().getId());
        request.setUserId(user.getId());
        request.setFileId(fileId);
        fileService.importFile(request);
    }

    @PutMapping("/{fileId}/rename")
    @Operation(summary = "重命名文件")
    public void rename(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(hidden = true) @PathInject PathRequest pathRequest,
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody FileRenameRequest request) {
        request.setModuleId(pathRequest.getModule().getId());
        request.setUserId(user.getId());
        request.setFileId(fileId);
        fileService.rename(request);
    }

    @PostMapping("/{fileId}/update")
    @Operation(summary = "更新文件")
    @Recount
    public void updateFile(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(hidden = true) @PathInject PathRequest pathRequest,
            @Parameter(hidden = true) @CurrentUser SessionUser user,
            @RequestBody FileUploadRequest request) {
        request.setModuleId(pathRequest.getModule().getId());
        request.setUserId(user.getId());
        fileService.updateFile(request);
    }

}
