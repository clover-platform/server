package plus.xyc.server.main.account.service.impl;

import com.github.pagehelper.Page;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.main.account.entity.dto.AccountAccessToken;
import plus.xyc.server.main.account.mapper.AccountAccessTokenMapper;
import plus.xyc.server.main.account.service.AccountAccessTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-11-27
 */
@Service
public class AccountAccessTokenServiceImpl extends ServiceImpl<AccountAccessTokenMapper, AccountAccessToken> implements AccountAccessTokenService {

    @Override
    public PageResult<AccountAccessToken> list(PageRequest pr, Long userId) {
        try(Page<AccountAccessToken> page = pr.start()) {
            baseMapper.findByAccountId(userId);
            return PageResult.of(page);
        }
    }
}
