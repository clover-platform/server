package plus.xyc.server.i18n.module.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新请求")
public class ModuleUpdateRequest {

    @Schema(description = "ID", hidden = true)
    private Long id;
    @Schema(description = "模块名称")
    private String name;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "用户ID", hidden = true)
    private Long userId;

}
