package plus.xyc.server.i18n.entry.entity.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "词条请求")
public class EntryRequest {

    @Schema(description = "键")
    private String identifier;
    @Schema(description = "值")
    private String value;
    @Schema(description = "上下文描述")
    private String context;

    @Schema(description = "翻译结果")
    private List<Result> results;

    @Data
    @Schema(description = "翻译结果")
    public static class Result {
        @Schema(description = "键")
        private String language;
        @Schema(description = "值")
        private String content;
    }
    
}
