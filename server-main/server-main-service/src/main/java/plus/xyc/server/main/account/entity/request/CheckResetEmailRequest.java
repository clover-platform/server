package plus.xyc.server.main.account.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "校验邮件")
public class CheckResetEmailRequest {

    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "验证码")
    private String code;

}
