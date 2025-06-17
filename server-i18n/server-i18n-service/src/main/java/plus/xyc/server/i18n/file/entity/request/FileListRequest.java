package plus.xyc.server.i18n.file.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "查询所有文件")
public class FileListRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;
    @Schema(description = "关键词", hidden = true)
    private String keyword;

}
