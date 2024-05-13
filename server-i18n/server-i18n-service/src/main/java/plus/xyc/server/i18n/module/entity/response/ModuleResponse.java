package plus.xyc.server.i18n.module.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "模块响应")
public class ModuleResponse {

    @Schema(description = "模块名称")
    private String name;
    @Schema(description = "唯一标识")
    private String identifier;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "来源")
    private String source;
    @Schema(description = "成员数量")
    private Integer memberSize;
    @Schema(description = "目标数量")
    private Integer targetSize;
    @Schema(description = "更新时间")
    private java.util.Date updateTime;

}