package plus.xyc.server.i18n.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.file.entity.dto.File;
import plus.xyc.server.i18n.module.entity.dto.Module;

@Data
@Schema(description = "路径信息")
public class PathRequest {

    @Schema(description = "模块信息")
    private Module module;

    @Schema(description = "文件信息")
    private File file;

    @Schema(description = "词条信息")
    private Entry entry;

}
