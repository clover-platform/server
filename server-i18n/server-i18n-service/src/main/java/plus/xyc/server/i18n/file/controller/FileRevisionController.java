package plus.xyc.server.i18n.file.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.file.entity.response.FileRevisionResponse;
import plus.xyc.server.i18n.file.service.FileRevisionService;

/**
 * <p>
 * 变更 前端控制器
 * </p>
 *
 * @author generator
 * @since 2025-06-17
 */
@RestController
@RequestMapping("/{moduleName}/file/{fileId}/revision")
@Tag(name = "FileRevisionController", description = "文件变更")
@Slf4j
public class FileRevisionController {

    @Resource
    private FileRevisionService fileRevisionService;

    @GetMapping("/list")
    @Operation(summary = "列表")
    public List<FileRevisionResponse> list(
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(hidden = true) @PathInject PathRequest pathRequest) {
        return fileRevisionService.list(fileId);
    }

}
