package plus.xyc.server.main.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.server.account.api.entity.request.*;
import org.zkit.support.server.account.api.entity.response.TokenWithAccountResponse;
import org.zkit.support.server.account.api.rest.AuthAccountRestApi;
import org.zkit.support.server.mail.api.entity.request.CheckCodeRequest;
import org.zkit.support.server.mail.api.entity.request.SendCodeRequest;
import org.zkit.support.server.mail.api.rest.MailRestApi;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.boot.utils.StringUtils;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import org.zkit.support.server.account.api.entity.response.AccountResponse;
import org.zkit.support.server.account.api.entity.response.TokenResponse;
import plus.xyc.server.main.account.entity.enums.AccountCode;
import plus.xyc.server.main.account.entity.dto.Account;
import plus.xyc.server.main.account.entity.mapstruct.AccountMapStruct;
import plus.xyc.server.main.account.entity.request.CheckRegisterEmailRequest;
import plus.xyc.server.main.account.entity.request.CheckResetEmailRequest;
import plus.xyc.server.main.account.entity.request.RegisterRequest;
import plus.xyc.server.main.account.entity.request.SetCurrentRequest;
import plus.xyc.server.main.account.mapper.AccountMapper;
import plus.xyc.server.main.account.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
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
    private MailRestApi mailRestApi;
    @Resource
    private AuthAccountRestApi authAccountRestApi;
    @Resource
    private AccountMapStruct accountMapStruct;

    @Override
    @Cacheable(value = "account#1d", key = "#id")
    public Account findById(Long id) {
        return getById(id);
    }

    public void sendRegisterEmail(String email) {
        SendCodeRequest request = new SendCodeRequest();
        request.setEmail(email);
        request.setAction("register");
        mailRestApi.sendCode(request);
    }

    @Override
    public Boolean checkRegisterEmail(CheckRegisterEmailRequest request) {
        int size = baseMapper.countByEmail(request.getEmail());
        if(size > 0) {
            throw new ResultException(AccountCode.REGISTER_HAS.code, MessageUtils.get(AccountCode.REGISTER_HAS.key));
        }
        // 验证码是否正确
        CheckCodeRequest checkCodeRequest = new CheckCodeRequest();
        checkCodeRequest.setEmail(request.getEmail());
        checkCodeRequest.setCode(request.getCode());
        checkCodeRequest.setAction("register");
        Result<Boolean> checkResult = mailRestApi.check(checkCodeRequest);
        if(!checkResult.isSuccess() || !checkResult.getData()) {
            throw new ResultException(AccountCode.REGISTER_CODE.code, MessageUtils.get(AccountCode.REGISTER_CODE.key));
        }
        return checkResult.getData();
    }

    private void check(String email, String username) {
        if(this.hasUsername(username)) {
            throw new ResultException(AccountCode.REGISTER_HAS.code, MessageUtils.get(AccountCode.REGISTER_HAS.key));
        }
        if(this.hasEmail(email)) {
            throw new ResultException(AccountCode.REGISTER_HAS.code, MessageUtils.get(AccountCode.REGISTER_HAS.key));
        }
    }

    @Override
    @DistributedLock(value = "account", el = false)
    @Transactional
    public Account add(Account account) {
        this.check(account.getEmail(), account.getUsername());
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
    @DistributedLock(value = "account", el = false)
    @Transactional
    public TokenResponse register(RegisterRequest request) {
        this.check(request.getEmail(), request.getUsername());
        Result<TokenWithAccountResponse> result = authAccountRestApi.register(accountMapStruct.toAccountRegisterRequestFromRegisterRequest(request));
        if(result.isSuccess()) {
            Account account = new Account();
            account.setId(result.getData().getAccountId());
            account.setUsername(request.getUsername());
            account.setEmail(request.getEmail());
            this.save(account);
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
    public PageResult<ApiAccountResponse> query(ApiAccountListRequest request) {
        if(request.getIds() == null || request.getIds().isEmpty())
            return PageResult.of(0, List.of());
        try(Page<Account> page = PageHelper.startPage(request.getPage(), request.getSize())) {
            List<Account> accounts = baseMapper.query(request);
            return PageResult.of(page.getTotal(), accounts.stream().map(accountMapStruct::toApiAccountResponse).toList());
        }
    }

    public void sendResetEmail(String email) {
        SendCodeRequest request = new SendCodeRequest();
        request.setEmail(email);
        request.setAction("reset");
        mailRestApi.sendCode(request);
    }

    @Override
    public TokenResponse checkResetEmail(CheckResetEmailRequest request) {
        // 验证码是否正确
        CheckCodeRequest checkCodeRequest = new CheckCodeRequest();
        checkCodeRequest.setEmail(request.getEmail());
        checkCodeRequest.setCode(request.getCode());
        checkCodeRequest.setAction("reset");
        Result<Boolean> checkResult = mailRestApi.check(checkCodeRequest);
        if(!checkResult.isSuccess() || !checkResult.getData()) {
            throw new ResultException(AccountCode.RESET_CODE.code, MessageUtils.get(AccountCode.RESET_CODE.key));
        }
        Account account = baseMapper.findOneByEmail(request.getEmail());
        if(account == null) {
            throw new ResultException(AccountCode.ACCOUNT_NOT_EXIST.code, MessageUtils.get(AccountCode.ACCOUNT_NOT_EXIST.key));
        }
        return createTempToken(account.getId());
    }

    private TokenResponse createTempToken(Long id) {
        CreateTokenRequest createTokenRequest = new CreateTokenRequest();
        createTokenRequest.setId(id);
        createTokenRequest.setExpiresIn(5 * 60 * 1000L);
        Result<TokenResponse> result = authAccountRestApi.createToken(createTokenRequest);
        if(!result.isSuccess()) {
            throw ResultException.internal();
        }
        return result.getData();
    }

    @Override
    public TokenResponse resetPassword(ResetPasswordRequest request) {
        Result<TokenResponse> result = authAccountRestApi.resetPassword(request);
        if(!result.isSuccess()) {
            throw ResultException.internal();
        }
        return result.getData();
    }
}
