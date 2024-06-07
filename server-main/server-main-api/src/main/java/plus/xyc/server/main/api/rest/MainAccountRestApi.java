package plus.xyc.server.main.api.rest;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.main.api.constant.MainApi;
import plus.xyc.server.main.api.constant.MainApiRoute;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

import java.util.List;

@FeignClient(value = MainApi.APP_NAME)
@Service
public interface MainAccountRestApi {

    @GetMapping(value = MainApiRoute.ACCOUNT_GET_BY_ID)
    Result<ApiAccountResponse> getById(@RequestParam("id") Long id);

    @GetMapping(value = MainApiRoute.ACCOUNT_LIST)
    Result<PageResult<ApiAccountResponse>> list(@SpringQueryMap @ParameterObject @ModelAttribute ApiAccountListRequest request);

}
