package plus.xyc.server.i18n.branch.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "查询所有分支")
public class BranchListRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;
    @Schema(description = "关键词", hidden = true)
    private String keyword;

}
