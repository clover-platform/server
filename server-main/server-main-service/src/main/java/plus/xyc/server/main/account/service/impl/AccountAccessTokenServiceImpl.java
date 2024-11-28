package plus.xyc.server.main.account.service.impl;

import com.github.pagehelper.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MD5Utils;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.main.account.entity.dto.AccountAccessToken;
import plus.xyc.server.main.account.entity.mapstruct.AccountAccessTokenMapStruct;
import plus.xyc.server.main.account.entity.request.AccountAccessTokenCreateRequest;
import plus.xyc.server.main.account.entity.request.AccountAccessTokenRevokeRequest;
import plus.xyc.server.main.account.mapper.AccountAccessTokenMapper;
import plus.xyc.server.main.account.service.AccountAccessTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.main.enums.MainCode;

import java.util.Date;
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
    public void revoke(AccountAccessTokenRevokeRequest request) {
        AccountAccessToken token = getById(request.getTokenId());
        if(!token.getAccountId().equals(request.getAccountId())) {
            throw new ResultException(MainCode.ACCESS_TOKEN_REJECT.code, MessageUtils.get(MainCode.ACCESS_TOKEN_REJECT.key));
        }
        removeById(request.getTokenId());
    }
}
