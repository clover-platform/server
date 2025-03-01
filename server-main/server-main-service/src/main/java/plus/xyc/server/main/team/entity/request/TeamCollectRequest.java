package plus.xyc.server.main.team.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "收藏团队信息")
public class TeamCollectRequest {

    @Schema(description = "团队ID")
    private Long id;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

}
