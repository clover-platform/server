package plus.xyc.server.i18n.file.entity.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "批量添加词条")
public class FileEntryBatchRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;

    @Schema(description = "文件ID列表")
    private List<Long> fileIdList;

    @Schema(description = "词条列表")
    private List<FileEntryBatchRequestEntry> entries;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

    @Data
    @Schema(description = "词条")
    public static class FileEntryBatchRequestEntry {

        @Schema(description = "唯一标识")
        private String identifier;

        @Schema(description = "内容")
        private String value;

        @Schema(description = "上下文")
        private String context;
    }
}
