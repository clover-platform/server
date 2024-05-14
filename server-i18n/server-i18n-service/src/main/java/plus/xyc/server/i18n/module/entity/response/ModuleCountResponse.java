package plus.xyc.server.i18n.module.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "模块信息统计")
public class ModuleCountResponse {

    @Schema(description = "词条数")
    private Integer wordCount;
    @Schema(description = "分支数")
    private Integer branchCount;
    @Schema(description = "成员数")
    private Integer memberCount;
    @Schema(description = "目标语言数")
    private Integer targetCount;

}
