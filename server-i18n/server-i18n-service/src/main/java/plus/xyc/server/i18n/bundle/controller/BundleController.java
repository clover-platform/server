package plus.xyc.server.i18n.bundle.controller;

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
import plus.xyc.server.i18n.bundle.entity.dto.Bundle;
import plus.xyc.server.i18n.bundle.entity.request.BundleCreateRequest;
import plus.xyc.server.i18n.bundle.entity.request.BundleQueryRequest;
import plus.xyc.server.i18n.bundle.service.BundleService;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.entity.PathRequest;

/**
 * <p>
 * 文件包 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/{moduleName}/bundle")
@Tag(name = "bundle", description = "下载包")
public class BundleController {

    @Resource
    private BundleService bundleService;

    @GetMapping("/list")
    @Operation(summary = "查询词条")
    public PageResult<Bundle> list(
            @ParameterObject @ModelAttribute PageRequest page,
            @ParameterObject @ModelAttribute BundleQueryRequest request,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest
    ) {
        request.setModuleId(pathRequest.getModule().getId());
        return bundleService.query(page, request);
    }

    @PostMapping("/create")
    @Operation(summary = "创建")
    public void create(
            @RequestBody BundleCreateRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @Parameter(description = "模块标识") @PathVariable String moduleName,
            @PathInject PathRequest pathRequest
    ) {
        request.setModuleId(pathRequest.getModule().getId());
        request.setUserId(user.getId());
        bundleService.create(request);
    }

}
