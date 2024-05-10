package plus.xyc.server.main.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.zkit.support.cloud.starter.entity.Result;
import org.zkit.support.cloud.starter.exception.ResultException;
import org.zkit.support.cloud.starter.service.EmailCodeService;
import org.zkit.support.cloud.starter.utils.MessageUtils;
import org.zkit.support.redisson.starter.DistributedLock;
import org.zkit.support.server.account.api.entity.request.AccountAddRequest;
import org.zkit.support.server.account.api.entity.request.CreateTokenRequest;
import org.zkit.support.server.account.api.entity.request.SetPasswordRequest;
import org.zkit.support.server.account.api.entity.response.AccountResponse;
import org.zkit.support.server.account.api.entity.response.TokenResponse;
import org.zkit.support.server.account.api.rest.AuthAccountApi;
import plus.xyc.server.main.account.entity.code.AccountCode;
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
    @Resource
    private AuthAccountApi authAccountApi;

    public void sendRegisterEmail(String email) {
        emailCodeService.send(email, "register");
    }

    @Override
    @DistributedLock(value = "account")
    public TokenResponse checkRegisterEmail(CheckRegisterEmailRequest request) {
        // 验证码是否正确
        boolean checked = emailCodeService.check(request.getEmail(), request.getCode(), "register");
        if(!checked) {
            throw new ResultException(AccountCode.REGISTER_CODE.code, MessageUtils.get(AccountCode.REGISTER_CODE.key));
        }
        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setUsername(request.getUsername());
        account = this.add(account);
        CreateTokenRequest createTokenRequest = new CreateTokenRequest();
        createTokenRequest.setId(account.getId());
        createTokenRequest.setExpiresIn(5 * 60 * 1000L);
        Result<TokenResponse> result = authAccountApi.createToken(createTokenRequest);
        if(!result.isSuccess()) {
            throw ResultException.internal();
        }
        return result.getData();
    }

    @Override
    @DistributedLock(value = "account")
    public Account add(Account account) {
        boolean has = this.hasUsername(account.getUsername());
        if(has) {
            throw new ResultException(AccountCode.REGISTER_HAS.code, MessageUtils.get(AccountCode.REGISTER_HAS.key));
        }
        has = this.hasEmail(account.getEmail());
        if(has) {
            throw new ResultException(AccountCode.REGISTER_HAS.code, MessageUtils.get(AccountCode.REGISTER_HAS.key));
        }

        AccountAddRequest request = new AccountAddRequest();
        request.setUsername(account.getUsername());
        Result<AccountResponse> result = authAccountApi.addOrGet(request);
        if(!result.isSuccess()) {
            throw ResultException.internal();
        }
        AccountResponse response = result.getData();
        account.setId(response.getId());
        this.saveOrUpdate(account);
        return account;
    }

    @Override
    @CacheEvict(value = "account", key = "#result.username")
    @DistributedLock(value = "account", key = "#request.id")
    public TokenResponse setPassword(SetPasswordRequest request) {
        Result<TokenResponse> result = authAccountApi.setPassword(request);
        if(result.isSuccess()) {
            // 更新用户信息
            UpdateWrapper<Account> update = new UpdateWrapper<>();
            update.eq("id",request.getId());
            update.set("active", 1);
            this.update(update);
        }else{
            throw new ResultException(result.getCode(),result.getMessage());
        }
        return result.getData();
    }

    private boolean hasUsername(String username) {
        return this.baseMapper.countByUsername(username) > 0;
    }


    private boolean hasEmail(String email) {
        return this.baseMapper.countByEmail(email) > 0;
    }

    @Autowired
    public void setEmailCodeService(EmailCodeService emailCodeService) {
        this.emailCodeService = emailCodeService;
    }
}
