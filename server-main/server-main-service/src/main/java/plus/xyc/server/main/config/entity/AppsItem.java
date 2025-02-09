package plus.xyc.server.main.config.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "AppsItem", description = "应用")
public class AppsItem {

    @Schema(description = "名称")
    private String title;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "URL")
    private String url;
    @Schema(description = "图标")
    private String icon;

}
