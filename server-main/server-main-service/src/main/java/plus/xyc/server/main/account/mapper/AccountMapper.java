package plus.xyc.server.main.account.mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.main.account.entity.dto.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-02
 */
public interface AccountMapper extends BaseMapper<Account> {

    int countByEmail(@Param("email") String email);
    int countByUsername(@Param("username") String username);
    Account findOneByEmail(@Param("email") String email);
    List<Account> query(IPage<Account> page, @Param("request") ApiAccountListRequest request);

}
