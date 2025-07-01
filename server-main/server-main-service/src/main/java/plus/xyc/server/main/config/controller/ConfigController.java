package plus.xyc.server.main.config.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.starter.security.annotation.PublicRequest;
import plus.xyc.server.main.config.entity.response.CommonConfigResponse;
import plus.xyc.server.main.config.service.ConfigService;

@RestController
@RequestMapping("/config")
@Tag(name = "ConfigController", description = "配置")
@Slf4j
public class ConfigController {

    @Resource
    private ConfigService configService;

    @PublicRequest
    @GetMapping("/common")
    @Operation(summary = "通用配置")
    public CommonConfigResponse common() {
        return configService.common();
    }

}
