package plus.xyc.server.main.team.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "查询团队")
public class TeamListRequest {

    @Schema(description = "类型 all:全部 create:我创建的 join:我加入的")
    private String type;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

}
