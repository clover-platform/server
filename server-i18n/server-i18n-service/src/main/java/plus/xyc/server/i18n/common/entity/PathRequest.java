package plus.xyc.server.i18n.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import plus.xyc.server.i18n.branch.entity.dto.Branch;
import plus.xyc.server.i18n.module.entity.dto.Module;

@Data
@Schema(description = "路径信息")
public class PathRequest {

    @Schema(description = "模块信息")
    private Module module;

    @Schema(description = "分支信息")
    private Branch branch;

}
