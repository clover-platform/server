package plus.xyc.server.i18n.file.entity.request;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "上传文件")
public class FileImportRequest {
    
    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

    @Schema(description = "文件ID", hidden = true)
    private Long fileId;

    @Schema(description = "配置")
    private Map<Integer, String> config;

}
