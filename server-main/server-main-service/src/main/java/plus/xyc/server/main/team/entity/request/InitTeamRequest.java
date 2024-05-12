package plus.xyc.server.main.team.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "初始化团队")
public class InitTeamRequest {

    @Schema(description = "团队名称")
    private String name;

    @Schema(description = "默认项目名称")
    private String projectName;

    @Schema(description = "所有者", hidden = true)
    private Long ownerId;

}
