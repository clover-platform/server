package plus.xyc.server.i18n.entry.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "词条创建请求")
public class EntryCreateRequest {

    @Schema(description = "文件ID")
    private List<Long> files;
    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;
    @Schema(description = "词条键")
    private String key;
    @Schema(description = "词条值")
    private String value;
    @Schema(description = "用户ID", hidden = true)
    private Long userId;

}
