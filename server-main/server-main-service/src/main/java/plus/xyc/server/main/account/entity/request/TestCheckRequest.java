package plus.xyc.server.main.account.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "测试表单验证")
public class TestCheckRequest {

    @NotNull
    @Min(100)
    @Schema(description = "大小")
    private Integer size;

    @Email
    @NotNull
    @Schema(description = "邮箱")
    private String email;

}
