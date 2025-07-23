package plus.xyc.server.i18n.file.entity.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "词条搜索")
public class FileEntrySearchRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;

    @Schema(description = "文件ID", example = "1,2,3")
    private String fileId;

    @Schema(description = "文件ID列表", hidden = true)
    private List<Long> fileIdList;

    @Schema(description = "搜索词", hidden = true)
    private String keyword;

}
