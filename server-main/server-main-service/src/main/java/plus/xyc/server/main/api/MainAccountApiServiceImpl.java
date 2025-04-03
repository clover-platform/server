package plus.xyc.server.main.api;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.main.account.entity.mapstruct.AccountMapStruct;
import plus.xyc.server.main.account.service.AccountAccessTokenService;
import plus.xyc.server.main.account.service.AccountService;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.service.MainAccountApiService;

@DubboService
public class MainAccountApiServiceImpl implements MainAccountApiService {

    @Resource
    private AccountService accountService;
    @Resource
    private AccountMapStruct accountMapStruct;
    @Resource
    private AccountAccessTokenService accountAccessTokenService;

    @Override
    public ApiAccountResponse getById(Long id) {
        return accountMapStruct.toApiAccountResponse(accountService.getById(id));
    }

    @Override
    public PageResult<ApiAccountResponse> list(ApiAccountListRequest request) {
         return accountService.query(request);
    }

    @Override
    public SessionUser checkAccessToken(String token) {
        return accountAccessTokenService.checkAccessToken(token);
    }
}
