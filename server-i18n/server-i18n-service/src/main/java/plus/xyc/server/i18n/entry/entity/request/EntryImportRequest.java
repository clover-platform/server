package plus.xyc.server.i18n.entry.entity.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "词条导入请求")
public class EntryImportRequest {

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "文件URL")
    private String fileUrl;

    @Schema(description = "词条列表")
    private List<Entry> entries;

    @Data
    @Schema(description = "词条")
    public static class Entry {
        @Schema(description = "键")
        private String identifier;
        @Schema(description = "值")
        private String value;
        @Schema(description = "上下文描述")
        private String context;

        @Schema(description = "翻译结果")
        private List<Result> results;
    }

    @Data
    @Schema(description = "翻译结果")
    public static class Result {
        @Schema(description = "键")
        private String language;
        @Schema(description = "值")
        private String content;
    }

}
