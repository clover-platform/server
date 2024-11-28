package plus.xyc.server.main.account.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "创建访问令牌")
public class AccountAccessTokenCreateRequest {

    @Schema(description = "账户ID", hidden = true)
    private Long accountId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "过期时间")
    private Date expirationTime;

    @Schema(description = "作用域")
    private List<String> scopes;

}
