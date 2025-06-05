package plus.xyc.server.main.account.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新头像")
public class UpdateAvatarRequest {
    
    @Schema(description = "账户ID", hidden = true)
    private Long id;

    @Schema(description = "URL")
    private String url;

}
