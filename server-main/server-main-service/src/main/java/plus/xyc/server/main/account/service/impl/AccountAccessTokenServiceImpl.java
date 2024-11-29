package plus.xyc.server.main.account.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.github.pagehelper.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MD5Utils;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.main.account.entity.dto.Account;
import plus.xyc.server.main.account.entity.dto.AccountAccessToken;
import plus.xyc.server.main.account.entity.mapstruct.AccountAccessTokenMapStruct;
import plus.xyc.server.main.account.entity.request.AccountAccessTokenCreateRequest;
import plus.xyc.server.main.account.entity.request.AccountAccessTokenRevokeRequest;
import plus.xyc.server.main.account.mapper.AccountAccessTokenMapper;
import plus.xyc.server.main.account.service.AccountAccessTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.main.account.service.AccountService;
import plus.xyc.server.main.enums.MainCode;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-11-27
 */
@Service
@Slf4j
public class AccountAccessTokenServiceImpl extends ServiceImpl<AccountAccessTokenMapper, AccountAccessToken> implements AccountAccessTokenService {

    @Resource
    private AccountAccessTokenMapStruct mapStruct;
    @Resource
    private AccountService accountService;

    @Override
    public PageResult<AccountAccessToken> list(PageRequest pr, Long userId) {
        try(Page<AccountAccessToken> page = pr.start()) {
            baseMapper.findByAccountId(userId);
            page.getResult().forEach(e -> e.setToken(null));
            return PageResult.of(page);
        }
    }

    @Override
    @DistributedLock(value = "'account:access:token:' + #request.accountId")
    @Transactional
    public String create(AccountAccessTokenCreateRequest request) {
        int size = baseMapper.countByAccountIdAndName(request.getAccountId(), request.getName());
        if (size > 0) {
            throw new ResultException(MainCode.ACCESS_TOKEN_NAME_EXISTS.code, MessageUtils.get(MainCode.ACCESS_TOKEN_NAME_EXISTS.key));
        }
        String token = MD5Utils.text(UUID.randomUUID().toString());
        AccountAccessToken accessToken = mapStruct.toAccountAccessToken(request);
        accessToken.setToken(token);
        accessToken.setCreateTime(new Date());
        save(accessToken);
        return token;
    }

    @Override
    @DistributedLock(value = "'account:access:token:' + #request.accountId")
    @Transactional
    @CacheEvict(value = "account:access:token", key = "#result")
    public String revoke(AccountAccessTokenRevokeRequest request) {
        AccountAccessToken token = getById(request.getTokenId());
        if(!token.getAccountId().equals(request.getAccountId())) {
            throw new ResultException(MainCode.ACCESS_TOKEN_REJECT.code, MessageUtils.get(MainCode.ACCESS_TOKEN_REJECT.key));
        }
        removeById(request.getTokenId());
        return token.getToken();
    }

    @Override
    @Cacheable(value = "account:access:token", key = "#token")
    public SessionUser checkAccessToken(String token) {
        AccountAccessToken accessToken = baseMapper.findOneByToken(token);
        if(accessToken == null)
            throw ResultException.unauthorized();
        Account account = accountService.getById(accessToken.getAccountId());
        SessionUser user = new SessionUser();
        user.setId(account.getId());
        user.setUsername(account.getUsername());
        user.setAuthorities(JSON.parseObject(JSON.toJSONString(accessToken.getScopes()), new TypeReference<List<String>>() {}));
        return user;
    }
}
