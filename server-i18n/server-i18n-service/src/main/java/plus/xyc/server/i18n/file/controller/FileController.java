package plus.xyc.server.i18n.file.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import plus.xyc.server.i18n.file.service.FileService; 
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.file.entity.dto.File;
import plus.xyc.server.i18n.file.entity.request.FileListRequest;

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
public class FileController {

    @Resource
    private FileService fileService;

    @GetMapping("/list")
    @Operation(summary = "查询文件")
    public PageResult<File> list(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest,
            @ParameterObject @ModelAttribute PageRequest page,
            @ParameterObject @ModelAttribute FileListRequest request) {
        request.setModuleId(pathRequest.getModule().getId());
        return fileService.list(page, request);
    }

}
