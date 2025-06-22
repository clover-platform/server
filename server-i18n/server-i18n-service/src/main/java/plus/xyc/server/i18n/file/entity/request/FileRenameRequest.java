package plus.xyc.server.i18n.file.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "重命名文件")
public class FileRenameRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "文件名")
    private String name;

}
