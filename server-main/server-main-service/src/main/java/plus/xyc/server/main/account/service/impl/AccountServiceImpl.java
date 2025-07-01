package plus.xyc.server.main.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.server.account.api.entity.request.*;
import org.zkit.support.server.account.api.entity.response.TokenWithAccountResponse;
import org.zkit.support.server.account.api.service.AccountApiService;
import org.zkit.support.server.account.api.service.AccountOTPApiService;
import org.zkit.support.server.message.api.entity.request.CheckCodeRequest;
import org.zkit.support.server.message.api.entity.request.SendCodeRequest;
import org.zkit.support.server.message.api.service.MailCodeApiService;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.boot.utils.StringUtils;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import org.zkit.support.server.account.api.entity.response.AccountResponse;
import org.zkit.support.server.account.api.entity.response.TokenResponse;
import plus.xyc.server.main.account.entity.dto.Account;
import plus.xyc.server.main.account.entity.dto.AccountReadme;
import plus.xyc.server.main.account.entity.mapstruct.AccountMapStruct;
import plus.xyc.server.main.account.entity.request.*;
import plus.xyc.server.main.account.entity.request.OTPBindRequest;
import plus.xyc.server.main.account.entity.request.OTPDisableRequest;
import plus.xyc.server.main.account.entity.response.AccountProfileResponse;
import plus.xyc.server.main.account.mapper.AccountMapper;
import plus.xyc.server.main.account.mapper.AccountReadmeMapper;
import plus.xyc.server.main.account.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.enums.MainCode;
import org.springframework.cache.annotation.Caching;

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
    private AccountMapStruct accountMapStruct;
    @DubboReference
    private AccountApiService accountApiService;
    @DubboReference
    private AccountOTPApiService accountOTPApiService;
    @Resource
    private AccountReadmeMapper accountReadmeMapper;
    @DubboReference
    private MailCodeApiService mailCodeApiService;

    @Override
    @Cacheable(value = "account#1h", key = "#id")
    public Account findById(Long id) {
        return getById(id);
    }

    public void sendRegisterEmail(String email) {
        SendCodeRequest request = new SendCodeRequest();
        request.setEmail(email);
        request.setAction("register");
        mailCodeApiService.send(request);
    }

    private void check(String email, String username) {
        if(this.hasUsername(username)) {
            throw new ResultException(MainCode.REGISTER_HAS.code, MessageUtils.get(MainCode.REGISTER_HAS.key));
        }
        if(this.hasEmail(email)) {
            throw new ResultException(MainCode.REGISTER_HAS.code, MessageUtils.get(MainCode.REGISTER_HAS.key));
        }
    }

    @Override
    @DistributedLock(value = "account", el = false)
    @Transactional
    public Account add(Account account) {
        this.check(account.getEmail(), account.getUsername());
        AccountAddRequest request = new AccountAddRequest();
        request.setUsername(account.getUsername());
        AccountResponse response = accountApiService.addOrGet(request);
        account.setId(response.getId());
        this.saveOrUpdate(account);
        return account;
    }

    @Override
    @DistributedLock(value = "'account:register:' + #request.email")
    @Transactional
    public TokenResponse register(RegisterRequest request) {
        this.check(request.getEmail(), request.getUsername());
        // 验证码是否正确
        CheckCodeRequest checkCodeRequest = new CheckCodeRequest();
        checkCodeRequest.setEmail(request.getEmail());
        checkCodeRequest.setCode(request.getCode());
        checkCodeRequest.setAction("register");
        Boolean checked = mailCodeApiService.check(checkCodeRequest);
        if(!checked) {
            throw new ResultException(MainCode.REGISTER_CODE.code, MessageUtils.get(MainCode.REGISTER_CODE.key));
        }
        TokenWithAccountResponse response = accountApiService.register(accountMapStruct.toAccountRegisterRequestFromRegisterRequest(request));
        Account account = new Account();
        account.setId(response.getAccountId());
        account.setUsername(request.getUsername());
        account.setEmail(request.getEmail());
        this.save(account);
        return response;
    }

    private boolean hasUsername(String username) {
        return this.baseMapper.countByUsername(username) > 0;
    }


    private boolean hasEmail(String email) {
        return this.baseMapper.countByEmail(email) > 0;
    }

    @Override
    public TokenResponse login(AccountLoginRequest request) {
        String username = request.getUsername();
        if(!StringUtils.isEmail(username)) {
            Account account = baseMapper.findOneByUsername(username);
            username = account.getEmail();
        }
        request.setUsername(username);
        return accountApiService.login(request);
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
        accountApiService.logout(token);
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
        mailCodeApiService.send(request);
    }

    @Override
    public TokenResponse checkResetEmail(CheckResetEmailRequest request) {
        // 验证码是否正确
        CheckCodeRequest checkCodeRequest = new CheckCodeRequest();
        checkCodeRequest.setEmail(request.getEmail());
        checkCodeRequest.setCode(request.getCode());
        checkCodeRequest.setAction("reset");
        Boolean checked = mailCodeApiService.check(checkCodeRequest);
        if(!checked) {
            throw new ResultException(MainCode.RESET_CODE.code, MessageUtils.get(MainCode.RESET_CODE.key));
        }
        Account account = baseMapper.findOneByEmail(request.getEmail());
        if(account == null) {
            throw new ResultException(MainCode.ACCOUNT_NOT_EXIST.code, MessageUtils.get(MainCode.ACCOUNT_NOT_EXIST.key));
        }
        return createTempToken(account.getId());
    }

    private TokenResponse createTempToken(Long id) {
        CreateTokenRequest createTokenRequest = new CreateTokenRequest();
        createTokenRequest.setId(id);
        createTokenRequest.setExpiresIn(5 * 60 * 1000L);
        return accountApiService.createToken(createTokenRequest);
    }

    @Override
    public TokenResponse resetPassword(ResetPasswordRequest request) {
        return accountApiService.resetPassword(request);
    }

    @Override
    @CacheEvict(value = {"account", "account:teams", "account:projects"}, key = "#request.id")
    public void changePassword(ChangePasswordRequest request, String token) {
        accountApiService.changePassword(request);
        accountApiService.logout(token);
    }

    @Override
    public void sendEmailCode(Long accountId, String action) {
        Account account = this.getById(accountId);
        SendCodeRequest request = new SendCodeRequest();
        request.setEmail(account.getEmail());
        request.setAction(action);
        mailCodeApiService.send(request);
    }

    private void checkEmailCode(CheckCodeRequest request) {
        Boolean checked = mailCodeApiService.check(request);
        if(!checked) {
            throw new ResultException(MainCode.EMAIL_CODE.code, MessageUtils.get(MainCode.EMAIL_CODE.key));
        }
    }

    @Override
    public void bindOTP(OTPBindRequest request) {
        Account account = this.getById(request.getAccountId());
        CheckCodeRequest checkCodeRequest = new CheckCodeRequest();
        checkCodeRequest.setEmail(account.getEmail());
        checkCodeRequest.setCode(request.getCode());
        checkCodeRequest.setAction("otp-enable");
        checkEmailCode(checkCodeRequest);

        org.zkit.support.server.account.api.entity.request.OTPBindRequest bindRequest = new org.zkit.support.server.account.api.entity.request.OTPBindRequest();
        bindRequest.setId(request.getAccountId());
        bindRequest.setCode(request.getOtpCode());
        accountOTPApiService.bind(bindRequest);
    }

    @Override
    public void disableOTP(OTPDisableRequest request) {
        Account account = this.getById(request.getAccountId());
        CheckCodeRequest checkCodeRequest = new CheckCodeRequest();
        checkCodeRequest.setEmail(account.getEmail());
        checkCodeRequest.setCode(request.getCode());
        checkCodeRequest.setAction("otp-disable");
        checkEmailCode(checkCodeRequest);

        org.zkit.support.server.account.api.entity.request.OTPDisableRequest disableRequest = new org.zkit.support.server.account.api.entity.request.OTPDisableRequest();
        disableRequest.setId(request.getAccountId());
        accountOTPApiService.disable(disableRequest);
    }

    @Override
    @Cacheable(value = "account:profile:by:username#1h", key = "#username")
    public AccountProfileResponse profile(String username) {
        Account account = baseMapper.findOneByUsername(username);
        if(account == null) {
            throw new ResultException(MainCode.ACCOUNT_NOT_EXIST.code, MessageUtils.get(MainCode.ACCOUNT_NOT_EXIST.key));
        }
        AccountReadme readme = accountReadmeMapper.selectById(account.getId());
        AccountProfileResponse response = accountMapStruct.toAccountProfileResponse(account);
        response.setReadme(readme == null ? null : readme.getContent());
        return response;
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "account#1h", key = "#request.id"),
        @CacheEvict(value = "account:profile:by:username#1h", key = "#result.username")
    })
    public Account updateAvatar(UpdateAvatarRequest request) {
        UpdateWrapper<Account> update = new UpdateWrapper<>();
        update.eq("id", request.getId());
        update.set("avatar", request.getUrl());
        baseMapper.update(update);
        return this.getById(request.getId());
    }
}
