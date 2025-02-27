package plus.xyc.server.main.account.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "发送邮件验证码请求")
public class SendEmailCodeRequest {

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "操作")
    private String action;

}
