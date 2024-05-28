package plus.xyc.server.i18n.entry.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "保存翻译")
public class EntryResultSaveRequest {

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "词条ID")
    private Long entryId;

    @Schema(description = "语言")
    private String language;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

}
