package plus.xyc.server.main.api.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "加入项目请求")
public class JoinProjectRequest {

    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "项目ID")
    private Long projectId;

}
