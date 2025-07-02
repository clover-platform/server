package plus.xyc.server.i18n.activity.service;

import org.springframework.stereotype.Service;
import org.zkit.support.server.message.api.configuration.MessageConfiguration;
import org.zkit.support.server.message.api.entity.request.ActivityRequest;
import org.zkit.support.server.message.api.service.ActivityService;

import com.alibaba.fastjson2.JSONObject;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.i18n.activity.entity.ActivityAction;
import plus.xyc.server.i18n.module.entity.dto.Module;

@Service
@Slf4j
public class ActivityLogService {

    @Resource
    private ActivityService activityService;
    @Resource
    private MessageConfiguration configuration;

    public void createModule(Module module) {
        JSONObject metadata = new JSONObject();
        metadata.put("module", module);
        ActivityRequest request = new ActivityRequest()
                .setApp(configuration.getApp())
                .setTitle(module.getName())
                .setUrl(module.getIdentifier() + "/dashboard")
                .setUserId(module.getOwner())
                .setAction(ActivityAction.CREATE_MODULE)
                .setMetadata(metadata);
        activityService.log(request);
    }

}
