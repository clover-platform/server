package plus.xyc.server.main.account.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建访问令牌")
public class AccountAccessTokenRevokeRequest {

    @Schema(description = "账户ID", hidden = true)
    private Long accountId;

    @Schema(description = "令牌ID")
    private Long tokenId;

}
