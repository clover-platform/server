package plus.xyc.server.main.config.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通用配置")
public class CommonConfigResponse {

    @Schema(description = "公钥")
    private String publicKey;
}
