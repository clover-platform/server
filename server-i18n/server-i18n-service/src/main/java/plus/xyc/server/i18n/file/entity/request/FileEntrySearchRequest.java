package plus.xyc.server.i18n.file.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "词条搜索")
public class FileEntrySearchRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "搜索词", hidden = true)
    private String keyword;

}
