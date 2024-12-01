package plus.xyc.server.i18n.open.entity.request;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "推送词条")
public class OpenEntryPushRequest {

    @Schema(description = "内容")
    private JSONObject content;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;

    @Schema(description = "分支ID", hidden = true)
    private Long branchId;

}
