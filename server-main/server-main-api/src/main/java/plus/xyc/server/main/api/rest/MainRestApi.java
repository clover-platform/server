package plus.xyc.server.main.api.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.zkit.support.starter.boot.entity.Result;
import plus.xyc.server.main.api.constant.MainApi;
import plus.xyc.server.main.api.constant.MainApiRoute;
import plus.xyc.server.main.api.entity.request.JoinProjectRequest;

@FeignClient(value = MainApi.APP_NAME)
@Service
public interface MainRestApi {

    @PostMapping(value = MainApiRoute.JOIN_PROJECT)
    Result<Boolean> joinProject(@RequestBody JoinProjectRequest request);

}
