package plus.xyc.server.i18n.entry.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "查询词条评论")
public class EntryCommentListRequest {

    @Schema(description = "词条ID", hidden = true)
    private Long entryId;
    @Schema(description = "语言")
    private String language;

}
