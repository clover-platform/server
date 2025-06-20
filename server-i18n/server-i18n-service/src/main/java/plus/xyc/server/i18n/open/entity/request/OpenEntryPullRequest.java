package plus.xyc.server.i18n.open.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "推送词条")
public class OpenEntryPullRequest {

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;

    @Schema(description = "文件ID", hidden = true)
    private Long fileId;

    @Schema(description = "语言")
    private String language;

}
