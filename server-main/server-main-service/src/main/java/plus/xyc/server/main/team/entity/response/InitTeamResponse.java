package plus.xyc.server.main.team.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "初始化团队信息")
public class InitTeamResponse {

    @Schema(description = "团队ID")
    private Long teamId;

}
