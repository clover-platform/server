package plus.xyc.server.main.config.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.security.annotation.PublicRequest;
import plus.xyc.server.main.config.entity.AppsItem;
import plus.xyc.server.main.configuration.AppsConfiguration;

import java.util.List;

@RestController
@RequestMapping("/config")
@Tag(name = "ConfigController", description = "配置")
@Slf4j
public class ConfigController {

    @Resource
    private AppsConfiguration appsConfiguration;

    @PublicRequest
    @GetMapping("/apps")
    @Operation(summary = "应用列表")
    public List<AppsItem> apps() {
        List<AppsItem> apps = JSON.parseObject(
            JSON.toJSONString(appsConfiguration.getList()),
            new TypeReference<List<AppsItem>>() {}
        );
        for (AppsItem app : apps) {
            app.setTitle(MessageUtils.get(app.getTitle()));
            app.setDescription(MessageUtils.get(app.getDescription()));
        }
        return apps;
    }

}
