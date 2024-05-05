package plus.xyc.server.main.account.service;

import plus.xyc.server.main.account.entity.dto.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.main.account.entity.request.CheckRegisterEmailRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-02
 */
public interface AccountService extends IService<Account> {

    void sendRegisterEmail(String email);
    void checkRegisterEmail(CheckRegisterEmailRequest request);
    Account add(Account account);

}
