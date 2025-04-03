package plus.xyc.server.main.api.service;

import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

public interface MainAccountApiService {

    ApiAccountResponse getById(Long id);
    PageResult<ApiAccountResponse> list(ApiAccountListRequest request);
    SessionUser checkAccessToken(String token);

}
