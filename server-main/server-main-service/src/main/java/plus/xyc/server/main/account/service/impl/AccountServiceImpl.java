package plus.xyc.server.main.account.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.boot.exception.ResultException;
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

    @Cacheable(value = "account", key = "#username")
    public Account findByUsername(String username) {
        Account account = getBaseMapper().findOneByUsername(username);
        if(account == null){
            ResultException ex = new ResultException(1, "test error");
            ex.setData(username);
            throw ex;
        }
        return account;
    }

}
