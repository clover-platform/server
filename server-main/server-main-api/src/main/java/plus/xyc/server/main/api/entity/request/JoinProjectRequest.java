package plus.xyc.server.main.api.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "加入项目请求")
public class JoinProjectRequest implements Serializable {

    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "项目ID")
    private Long projectId;

}
