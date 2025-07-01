package plus.xyc.server.main.config.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "应用")
public class AppResponse {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "唯一标识")
    private String appId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "地址")
    private String href;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "图片图标")
    private String iconUrl;

}
