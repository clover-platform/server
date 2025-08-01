package plus.xyc.server.i18n.activity.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.zkit.support.server.message.api.service.ActivityApiService;
import org.zkit.support.starter.mybatis.entity.PageResult;

import com.alibaba.fastjson2.JSONObject;

import jakarta.annotation.Resource;
import plus.xyc.server.i18n.activity.entity.mapstruct.ActivityMapStruct;
import plus.xyc.server.i18n.activity.entity.request.ActivityListRequest;
import plus.xyc.server.i18n.activity.entity.response.ActivityResponse;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.service.MainAccountApiService;

@Service("i18nActivityService")
public class ActivityService {

    @DubboReference
    private ActivityApiService activityApiService;
    @DubboReference
    private MainAccountApiService mainAccountApiService;
    @Resource
    private ActivityMapStruct activityMapStruct;

    public PageResult<ActivityResponse> list(ActivityListRequest request) {
        org.zkit.support.server.message.api.entity.request.ActivityListRequest alr = activityMapStruct.toActivityListRequest(request);
        ApiAccountResponse account = mainAccountApiService.getById(alr.getUserId());
        JSONObject metadata = new JSONObject();
        metadata.put("teamId", account.getCurrentTeamId());
        alr.setMetadata(metadata);
        PageResult<org.zkit.support.server.message.api.entity.response.ActivityResponse> pr = activityApiService.list(alr);
        List<org.zkit.support.server.message.api.entity.response.ActivityResponse> origActivityResponses = pr.getData();
        List<Long> userIds = origActivityResponses.stream().map(org.zkit.support.server.message.api.entity.response.ActivityResponse::getUserId).distinct().collect(Collectors.toList());
        ApiAccountListRequest aalr = new ApiAccountListRequest();
        aalr.setIds(userIds);
        PageResult<ApiAccountResponse> accounts = mainAccountApiService.list(aalr);
        List<ActivityResponse> activityResponses = origActivityResponses.stream().map(ar -> {
            ActivityResponse response = activityMapStruct.toActivityResponse(ar);
            response.setAccount(accounts.getData().stream().filter(a -> a.getId().equals(ar.getUserId())).findFirst().orElse(null));
            return response;
        }).collect(Collectors.toList());
        return PageResult.of(pr.getTotal(), activityResponses);
    }
}
