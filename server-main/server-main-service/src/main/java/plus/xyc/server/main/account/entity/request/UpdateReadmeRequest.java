package plus.xyc.server.main.account.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新Readme")
public class UpdateReadmeRequest {
    
    @Schema(description = "账户ID", hidden = true)
    private Long accountId;

    @Schema(description = "内容")
    private String content;

}
