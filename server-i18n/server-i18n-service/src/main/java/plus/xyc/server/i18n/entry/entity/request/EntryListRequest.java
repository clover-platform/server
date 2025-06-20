package plus.xyc.server.i18n.entry.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "词条请求")
public class EntryListRequest {

    @Schema(description = "文件ID")
    private Long fileId;
    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;
    @Schema(description = "语言")
    private String language;

}
