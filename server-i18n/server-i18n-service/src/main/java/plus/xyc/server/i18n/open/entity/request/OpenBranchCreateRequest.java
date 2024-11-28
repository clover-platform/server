package plus.xyc.server.i18n.open.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建分支")
public class OpenBranchCreateRequest {

    @Schema(description = "名称")
    private String name;

}
