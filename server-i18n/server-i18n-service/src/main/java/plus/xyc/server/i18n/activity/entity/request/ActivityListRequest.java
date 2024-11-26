package plus.xyc.server.i18n.activity.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "动态查询")
public class ActivityListRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;
    @Schema(description = "用户ID", hidden = true)
    private Long userId;

}
