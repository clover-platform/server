package plus.xyc.server.i18n.module.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建模块请求")
public class ModuleCreateRequest {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "标识")
    private String identifier;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "源语言")
    private String source;

    @Schema(description = "目标语言")
    private String[] targets;

    @Schema(description = "拥有者", hidden = true)
    private Long owner;

    @Schema(description = "当前项目ID", hidden = true)
    private Long projectId;

}
