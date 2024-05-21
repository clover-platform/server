package plus.xyc.server.i18n.branch.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建分支")
public class BranchCreateRequest {

    @Schema(description = "模块ID")
    private Long moduleId;
    @Schema(description = "类型 clone-克隆, empty-空分支")
    private String type;
    @Schema(description = "分支名称")
    private String name;

}
