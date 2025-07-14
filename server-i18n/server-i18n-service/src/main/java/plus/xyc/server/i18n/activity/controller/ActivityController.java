package plus.xyc.server.i18n.activity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.service.MainAccountApiService;

import java.util.HashMap;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.server.message.api.configuration.MessageConfiguration;
import org.zkit.support.server.message.api.entity.request.ActivityListRequest;
import org.zkit.support.server.message.api.entity.response.ActivityResponse;
import org.zkit.support.server.message.api.service.ActivityApiService;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;

import com.alibaba.fastjson2.JSONObject;

/**
 * <p>
 * 项目 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/activity")
@Tag(name = "ActivityController", description = "活动")
@Slf4j
public class ActivityController {

    @DubboReference
    private ActivityApiService activityApiService;
    @Resource
    private MessageConfiguration messageConfiguration;
    @DubboReference
    private MainAccountApiService mainAccountApiService;

    @GetMapping("/list")
    @Operation(summary = "列表")
    public PageResult<ActivityResponse> list(
            @ParameterObject @ModelAttribute ActivityListRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        ApiAccountResponse account = mainAccountApiService.getById(user.getId());
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("projectId", account.getCurrentProjectId());
        request.setMetadata(metadata);
        request.setApp(messageConfiguration.getApp());
        request.setUserId(user.getId());
        log.info("request: {}", request);
        return activityApiService.list(request);
    }
}
