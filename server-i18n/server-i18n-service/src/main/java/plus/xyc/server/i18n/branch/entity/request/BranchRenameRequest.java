package plus.xyc.server.i18n.branch.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "重命名")
public class BranchRenameRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;
    @Schema(description = "分支ID", hidden = true)
    private Long id;
    @Schema(description = "分支名称")
    private String name;

}
