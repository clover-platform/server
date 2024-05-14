package plus.xyc.server.main.api.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.zkit.support.starter.boot.entity.Result;
import plus.xyc.server.main.api.constant.MainApi;
import plus.xyc.server.main.api.constant.MainApiRoute;
import plus.xyc.server.main.api.entity.request.AccountRequest;
import plus.xyc.server.main.api.entity.response.AccountResponse;

import java.util.List;

@FeignClient(value = MainApi.APP_NAME)
@Service
public interface MainAccountRestApi {

    @GetMapping(value = MainApiRoute.ACCOUNT_GET_BY_ID)
    Result<AccountResponse> getById(@RequestParam("id") Long id);
    @GetMapping(value = MainApiRoute.ACCOUNT_LIST)
    Result<List<AccountResponse>> list(@RequestBody AccountRequest request);

}
