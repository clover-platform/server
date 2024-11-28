package plus.xyc.server.main.account.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.main.account.entity.dto.AccountAccessToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-11-27
 */
public interface AccountAccessTokenMapper extends BaseMapper<AccountAccessToken> {

    List<AccountAccessToken> findByAccountId(@Param("accountId") Long accountId);
    int countByAccountIdAndName(@Param("accountId") Long accountId, @Param("name") String name);

}
