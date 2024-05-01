package plus.xyc.server.main.account.mapper;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.main.account.entity.dto.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-02
 */
public interface AccountMapper extends BaseMapper<Account> {

    Account findOneByUsername(@Param("username") String username);

}
