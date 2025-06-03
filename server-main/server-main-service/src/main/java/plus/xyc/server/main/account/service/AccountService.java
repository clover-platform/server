package plus.xyc.server.main.account.service;

import org.zkit.support.server.account.api.entity.request.AccountLoginRequest;
import org.zkit.support.server.account.api.entity.request.ChangePasswordRequest;
import org.zkit.support.server.account.api.entity.request.ResetPasswordRequest;
import org.zkit.support.server.account.api.entity.response.TokenResponse;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.main.account.entity.dto.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.main.account.entity.request.*;
import plus.xyc.server.main.account.entity.response.AccountProfileResponse;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

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
    Account add(Account account);
    TokenResponse register(RegisterRequest request);
    TokenResponse login(AccountLoginRequest request);
    void setCurrent(SetCurrentRequest request);
    void logout(String token, Long accountId);
    PageResult<ApiAccountResponse> query(ApiAccountListRequest request);
    void sendResetEmail(String email);
    TokenResponse checkResetEmail(CheckResetEmailRequest request);
    TokenResponse resetPassword(ResetPasswordRequest request);
    void changePassword(ChangePasswordRequest request, String token);
    void sendEmailCode(Long accountId, String action);
    void bindOTP(OTPBindRequest request);
    void disableOTP(OTPDisableRequest request);
    AccountProfileResponse profile(String username);

}
