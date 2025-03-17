package plus.xyc.server.main.project.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "收藏项目信息")
public class ProjectCollectRequest {

    @Schema(description = "项目ID")
    private Long id;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

}
