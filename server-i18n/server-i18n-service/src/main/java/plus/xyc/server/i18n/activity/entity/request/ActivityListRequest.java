package plus.xyc.server.i18n.activity.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "活动列表请求")
public class ActivityListRequest {

    @Schema(description = "应用", hidden = true)
    private String app;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "页码", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页条数", defaultValue = "10")
    private Integer size = 10;

}
