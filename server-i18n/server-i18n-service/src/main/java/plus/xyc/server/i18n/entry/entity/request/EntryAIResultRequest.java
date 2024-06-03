package plus.xyc.server.i18n.entry.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "AI建议")
public class EntryAIResultRequest {

    @Schema(description = "词条ID")
    private Long entryId;

    @Schema(description = "语言")
    private String language;

}
