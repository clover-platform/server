package plus.xyc.server.i18n.branch.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "合并分支")
public class BranchMergeRequest {

    @Schema(description = "模块ID")
    private Long moduleId;
    @Schema(description = "分支ID", hidden = true)
    private Long id;
    @Schema(description = "是否合并后删除分支")
    private Boolean deleteAfterMerge;

}
