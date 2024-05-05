package plus.xyc.server.main.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkit.support.boot.exception.ResultException;
import org.zkit.support.boot.service.EmailCodeService;
import org.zkit.support.boot.utils.MessageUtils;
import plus.xyc.server.main.account.entity.dto.Account;
import plus.xyc.server.main.account.mapper.AccountMapper;
import plus.xyc.server.main.account.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-02
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    private EmailCodeService emailCodeService;

    public void sendRegisterEmail(String email) {
        if(!this.hasEmail(email)) {
            throw new ResultException(1, MessageUtils.get("mail.fail"));
        }
        emailCodeService.send(email, "register");
    }

    private boolean hasEmail(String email) {
        return this.baseMapper.countByEmail(email) == 0;
    }

    @Autowired
    public void setEmailCodeService(EmailCodeService emailCodeService) {
        this.emailCodeService = emailCodeService;
    }
}
