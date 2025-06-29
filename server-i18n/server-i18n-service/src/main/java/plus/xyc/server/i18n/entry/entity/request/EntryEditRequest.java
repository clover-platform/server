package plus.xyc.server.i18n.entry.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "词条更新请求")
public class EntryEditRequest {

    @Schema(description = "词条ID", hidden = true)
    private Long id;
    @Schema(description = "词条值")
    private String value;
    @Schema(description = "用户ID", hidden = true)
    private Long userId;
    @Schema(description = "上下文")
    private String context;

}
