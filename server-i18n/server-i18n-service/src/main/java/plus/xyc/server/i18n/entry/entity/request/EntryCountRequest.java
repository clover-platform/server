package plus.xyc.server.i18n.entry.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "词条数量统计请求")
public class EntryCountRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;
    @Schema(description = "语言")
    private String language;
    @Schema(description = "分支")
    private String branch;

}
