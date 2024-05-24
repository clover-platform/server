package plus.xyc.server.i18n.entry.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "词条创建请求")
public class EntryCreateRequest {

    @Schema(description = "词条名称")
    private List<String> branches;
    @Schema(description = "模块ID")
    private Long moduleId;
    @Schema(description = "词条键")
    private String key;
    @Schema(description = "词条值")
    private String value;
    @Schema(description = "用户ID", hidden = true)
    private Long userId;

}
