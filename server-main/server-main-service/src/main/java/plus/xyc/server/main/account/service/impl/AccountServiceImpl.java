package plus.xyc.server.main.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.server.account.api.entity.request.AccountLoginRequest;
import org.zkit.support.server.account.api.rest.AuthAccountRestApi;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.service.EmailCodeService;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.boot.utils.StringUtils;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import org.zkit.support.server.account.api.entity.request.AccountAddRequest;
import org.zkit.support.server.account.api.entity.request.CreateTokenRequest;
import org.zkit.support.server.account.api.entity.request.SetPasswordRequest;
import org.zkit.support.server.account.api.entity.response.AccountResponse;
import org.zkit.support.server.account.api.entity.response.TokenResponse;
import plus.xyc.server.main.account.entity.enums.AccountCode;
import plus.xyc.server.main.account.entity.dto.Account;
import plus.xyc.server.main.account.entity.mapstruct.AccountMapStruct;
import plus.xyc.server.main.account.entity.request.CheckRegisterEmailRequest;
import plus.xyc.server.main.account.entity.request.SetCurrentRequest;
import plus.xyc.server.main.account.mapper.AccountMapper;
import plus.xyc.server.main.account.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

import java.util.List;

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

    @Resource
    private EmailCodeService emailCodeService;
    @Resource
    private AuthAccountRestApi authAccountRestApi;
    @Resource
    private AccountMapStruct accountMapStruct;

    @Override
    @Cacheable(value = "account", key = "#id")
    public Account findById(Long id) {
        return getById(id);
    }

    public void sendRegisterEmail(String email) {
        emailCodeService.send(email, "register");
    }

    @Override
    @DistributedLock(value = "account", el = false)
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
        Result<TokenResponse> result = authAccountRestApi.createToken(createTokenRequest);
        if(!result.isSuccess()) {
            throw ResultException.internal();
        }
        return result.getData();
    }

    @Override
    @DistributedLock(value = "account", el = false)
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
        Result<AccountResponse> result = authAccountRestApi.addOrGet(request);
        if(!result.isSuccess()) {
            throw ResultException.internal();
        }
        AccountResponse response = result.getData();
        account.setId(response.getId());
        this.saveOrUpdate(account);
        return account;
    }

    @Override
    @CacheEvict(value = "account", key = "#request.id")
    @DistributedLock(value = "'account:'+#request.id")
    public TokenResponse setPassword(SetPasswordRequest request) {
        Result<TokenResponse> result = authAccountRestApi.setPassword(request);
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

    @Override
    public TokenResponse login(AccountLoginRequest request) {
        Account account = null;
        String username = request.getUsername();
        if(StringUtils.isEmail(username)) {
            account = baseMapper.findOneByEmail(username);
            username = account.getUsername();
        }
        account = null;
        QueryWrapper<Account> query = new QueryWrapper<>();
        query.eq("username", username)
                .eq("enable", true);
        account = this.getOne(query);
        if(account == null) {
            throw new ResultException(AccountCode.LOGIN_NOT_EXIST.code, MessageUtils.get(AccountCode.LOGIN_NOT_EXIST.key));
        }
        Result<TokenResponse> result = authAccountRestApi.login(request);
        if(!result.isSuccess()) {
            throw new ResultException(result.getCode(), result.getMessage());
        }
        return result.getData();
    }

    @Override
    @CacheEvict(value = {"account", "account:teams", "account:projects"}, key = "#request.accountId")
    @DistributedLock(value = "'account:'+#request.accountId")
    public void setCurrent(SetCurrentRequest request) {
        UpdateWrapper<Account> update = new UpdateWrapper<>();
        update.eq("id", request.getAccountId());
        update.set("current_team_id", request.getTeamId());
        update.set("current_project_id", request.getProjectId());
        this.update(update);
    }

    @Override
    @CacheEvict(value = {"account", "account:teams", "account:projects"}, key = "#accountId")
    public void logout(String token, Long accountId) {
        authAccountRestApi.logout(token);
    }

    @Override
    public PageResult<ApiAccountResponse> findByIds(List<Long> ids, Integer size, Integer page) {
        if(ids.isEmpty())
            return PageResult.of(0, List.of());
        Page<Account> p = new Page<>(page, size);
        List<Account> accounts = baseMapper.findByIds(p, ids);
        return PageResult.of(p.getTotal(), accounts.stream().map(accountMapStruct::toApiAccountResponse).toList());
    }
}
