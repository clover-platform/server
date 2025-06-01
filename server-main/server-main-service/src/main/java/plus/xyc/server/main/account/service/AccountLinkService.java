package plus.xyc.server.main.account.service;
import plus.xyc.server.main.account.entity.dto.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zkit.support.server.account.api.entity.request.AuthLinkBindRequest;
import org.zkit.support.server.account.api.entity.response.TokenResponse;

public interface AccountLinkService extends IService<Account> {
    TokenResponse bind(AuthLinkBindRequest request);
}
