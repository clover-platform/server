package plus.xyc.server.main.account.service;

import org.zkit.support.server.account.api.entity.request.AccountLoginRequest;
import org.zkit.support.server.account.api.entity.request.SetPasswordRequest;
import org.zkit.support.server.account.api.entity.response.TokenResponse;
import plus.xyc.server.main.account.entity.dto.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.main.account.entity.request.CheckRegisterEmailRequest;
import plus.xyc.server.main.account.entity.request.SetCurrentRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-02
 */
public interface AccountService extends IService<Account> {

    Account findById(Long id);
    void sendRegisterEmail(String email);
    TokenResponse checkRegisterEmail(CheckRegisterEmailRequest request);
    Account add(Account account);
    TokenResponse setPassword(SetPasswordRequest request);
    TokenResponse login(AccountLoginRequest request);
    void setCurrent(SetCurrentRequest request);
    void logout(String token, Long accountId);

}
