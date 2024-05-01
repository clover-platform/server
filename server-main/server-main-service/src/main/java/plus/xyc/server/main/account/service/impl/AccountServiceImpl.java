package plus.xyc.server.main.account.service.impl;

import org.springframework.cache.annotation.Cacheable;
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

    @Override
    @Cacheable(value = "account", key = "#username")
    public Account findByUsername(String username) {
        return getBaseMapper().findOneByUsername(username);
    }
}
