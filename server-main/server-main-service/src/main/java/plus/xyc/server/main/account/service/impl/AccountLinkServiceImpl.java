package plus.xyc.server.main.account.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import plus.xyc.server.main.account.service.AccountLinkService;
import plus.xyc.server.main.enums.MainCode;
import plus.xyc.server.main.account.entity.dto.Account;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.main.account.mapper.AccountMapper;
import org.zkit.support.server.account.api.entity.request.AuthLinkBindRequest;
import org.zkit.support.server.account.api.entity.response.TokenResponse;
import org.zkit.support.server.account.api.service.AuthAccountLinkService;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;

@Service
@Slf4j
public class AccountLinkServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountLinkService {

    @DubboReference
    private AuthAccountLinkService authAccountLinkService;

    @Override
    public TokenResponse bind(AuthLinkBindRequest request) {
        Account account = getBaseMapper().findOneByUsername(request.getAccount());
        if(account == null) {
            throw new ResultException(MainCode.ACCOUNT_NOT_EXIST.code, MessageUtils.get(MainCode.ACCOUNT_NOT_EXIST.key));
        }
        request.setAccount(account.getEmail());
        return authAccountLinkService.bind(request);
    }
}