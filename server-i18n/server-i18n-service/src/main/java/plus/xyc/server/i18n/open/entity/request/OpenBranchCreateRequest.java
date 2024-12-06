package plus.xyc.server.i18n.open.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建分支")
public class OpenBranchCreateRequest {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;

    @Schema(description = "是否克隆")
    private Boolean clone;

}
