package plus.xyc.server.i18n.branch.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分支合并预览")
public class BranchMergeOverviewResponse {

    @Schema(description = "删除")
    private Integer deleted;
    @Schema(description = "添加")
    private Integer added;
    @Schema(description = "修改")
    private Integer changed;

}
