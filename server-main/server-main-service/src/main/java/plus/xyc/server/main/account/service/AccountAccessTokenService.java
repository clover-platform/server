package plus.xyc.server.main.account.service;

import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.main.account.entity.dto.AccountAccessToken;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.main.account.entity.request.AccountAccessTokenCreateRequest;
import plus.xyc.server.main.account.entity.request.AccountAccessTokenRevokeRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generator
 * @since 2024-11-27
 */
public interface AccountAccessTokenService extends IService<AccountAccessToken> {

    PageResult<AccountAccessToken> list(PageRequest page, Long userId);
    String create(AccountAccessTokenCreateRequest request);
    String revoke(AccountAccessTokenRevokeRequest request);
    SessionUser checkAccessToken(String token);

}
