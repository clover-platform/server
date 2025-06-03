package plus.xyc.server.main.account.service.impl;

import plus.xyc.server.main.account.entity.dto.Account;
import plus.xyc.server.main.account.entity.dto.AccountReadme;
import plus.xyc.server.main.account.entity.request.UpdateReadmeRequest;
import plus.xyc.server.main.account.mapper.AccountMapper;
import plus.xyc.server.main.account.mapper.AccountReadmeMapper;
import plus.xyc.server.main.account.service.AccountReadmeService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2025-06-03
 */
@Service
public class AccountReadmeServiceImpl extends ServiceImpl<AccountReadmeMapper, AccountReadme> implements AccountReadmeService {

    @Resource
    private AccountMapper accountMapper;

    @Override
    @CacheEvict(value = "account:profile:by:username#1h", key = "#result.username")
    public Account update(UpdateReadmeRequest request) {
        // 根据 accountId 查询是否存在
        AccountReadme accountReadme = getBaseMapper().selectOne(new LambdaQueryWrapper<AccountReadme>()
                .eq(AccountReadme::getId, request.getAccountId()));
        // 如果为空，则插入
        if (accountReadme == null) {
            accountReadme = new AccountReadme();
            accountReadme.setId(request.getAccountId());
            accountReadme.setContent(request.getContent());
            getBaseMapper().insert(accountReadme);
        } else{
            Wrapper<AccountReadme> wrapper = new LambdaUpdateWrapper<AccountReadme>()
                    .eq(AccountReadme::getId, request.getAccountId())
                    .set(AccountReadme::getContent, request.getContent());
            getBaseMapper().update(null, wrapper);
        }

        return accountMapper.selectById(request.getAccountId());
    }

}
