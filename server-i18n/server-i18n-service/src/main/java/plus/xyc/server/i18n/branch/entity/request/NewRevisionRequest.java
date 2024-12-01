package plus.xyc.server.i18n.branch.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建新版本")
public class NewRevisionRequest {

    @Schema(description = "分支ID")
    private Long branchId;
    @Schema(description = "创建人")
    private Long userId;
    @Schema(description = "版本名称")
    private String message;

}
