package plus.xyc.server.main.config.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.security.annotation.PublicRequest;
import plus.xyc.server.main.config.entity.dto.ConfigApp;
import plus.xyc.server.main.config.entity.mapstruct.ConfigAppMapStruct;
import plus.xyc.server.main.config.entity.reponse.AppResponse;
import plus.xyc.server.main.config.service.ConfigAppService;

import java.util.List;

/**
 * <p>
 * 应用 前端控制器
 * </p>
 *
 * @author generator
 * @since 2025-02-20
 */
@RestController
@RequestMapping("/config/app")
@Tag(name = "ConfigAppController", description = "应用配置")
@Slf4j
public class ConfigAppController {

    @Resource
    private ConfigAppService configAppService;
    @Resource
    private ConfigAppMapStruct mapStruct;

    @PublicRequest
    @GetMapping("/list")
    @Operation(summary = "应用列表")
    public List<AppResponse> apps() {
        List<ConfigApp> apps = configAppService.all();
        return apps.stream().map((app) -> {
            AppResponse ar = mapStruct.toAppResponse(app);
            ar.setName(MessageUtils.get(app.getNameKey()));
            ar.setDescription(MessageUtils.get(app.getDescriptionKey()));
            return ar;
        }).toList();
    }

}
