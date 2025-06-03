package plus.xyc.server.main.account.service;

import plus.xyc.server.main.account.entity.dto.Account;
import plus.xyc.server.main.account.entity.dto.AccountReadme;
import plus.xyc.server.main.account.entity.request.UpdateReadmeRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generator
 * @since 2025-06-03
 */
public interface AccountReadmeService extends IService<AccountReadme> {

    Account update(UpdateReadmeRequest request);

}
