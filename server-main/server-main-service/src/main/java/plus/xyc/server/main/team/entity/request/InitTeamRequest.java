package plus.xyc.server.main.team.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "初始化团队")
public class InitTeamRequest {

    @Schema(description = "团队名称")
    private String name;

    @Schema(description = "团队标识")
    private String key;

    @Schema(description = "所有者", hidden = true)
    private Long ownerId;

}
