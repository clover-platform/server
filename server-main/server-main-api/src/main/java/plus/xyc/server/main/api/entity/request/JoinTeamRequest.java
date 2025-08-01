package plus.xyc.server.main.api.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "加入项目请求")
public class JoinTeamRequest implements Serializable {

    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "团队ID")
    private Long teamId;

}
