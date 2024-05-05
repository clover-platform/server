package plus.xyc.server.main.account.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkit.support.cloud.starter.exception.ResultException;
import org.zkit.support.cloud.starter.service.EmailCodeService;
import org.zkit.support.cloud.starter.utils.MessageUtils;
import org.zkit.support.redisson.starter.DistributedLock;
import plus.xyc.server.main.account.entity.dto.Account;
import plus.xyc.server.main.account.entity.request.CheckRegisterEmailRequest;
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
@Slf4j
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    private EmailCodeService emailCodeService;

    public void sendRegisterEmail(String email) {
        if(!this.hasEmail(email)) {
            throw new ResultException(1, MessageUtils.get("mail.fail"));
        }
        emailCodeService.send(email, "register");
    }

    @Override
    public void checkRegisterEmail(CheckRegisterEmailRequest request) {
        // 验证码是否正确
        boolean checked = emailCodeService.check(request.getEmail(), request.getCode(), "register");
        if(!checked) {
            throw new ResultException(1, MessageUtils.get("public.code.fail"));
        }
        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setUsername(request.getUsername());
        account = this.add(account);
    }

    @Override
    @DistributedLock(value = "account")
    public Account add(Account account) {
        log.info("account {}", account);
        return null;
    }

    private boolean hasEmail(String email) {
        return this.baseMapper.countByEmail(email) == 0;
    }

    @Autowired
    public void setEmailCodeService(EmailCodeService emailCodeService) {
        this.emailCodeService = emailCodeService;
    }
}
