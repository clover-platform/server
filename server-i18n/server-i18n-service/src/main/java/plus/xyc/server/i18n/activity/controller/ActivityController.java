package plus.xyc.server.i18n.activity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.i18n.activity.entity.request.ActivityListRequest;
import plus.xyc.server.i18n.activity.entity.response.ActivityResponse;
import plus.xyc.server.i18n.activity.service.ActivityService;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.server.message.api.configuration.MessageConfiguration;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;

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

    @Resource
    private MessageConfiguration messageConfiguration;
    @Resource
    private ActivityService activityService;

    @GetMapping("/list")
    @Operation(summary = "列表")
    public PageResult<ActivityResponse> list(
            @ParameterObject @ModelAttribute ActivityListRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setApp(messageConfiguration.getApp());
        request.setUserId(user.getId());
        return activityService.list(request);
    }
}
