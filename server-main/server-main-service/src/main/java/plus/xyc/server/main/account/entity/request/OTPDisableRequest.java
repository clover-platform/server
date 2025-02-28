package plus.xyc.server.main.account.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "绑定OTP请求")
public class OTPDisableRequest {

    @Schema(description = "邮箱验证码")
    private String code;

    @Schema(description = "账户ID", hidden = true)
    private Long accountId;

}
