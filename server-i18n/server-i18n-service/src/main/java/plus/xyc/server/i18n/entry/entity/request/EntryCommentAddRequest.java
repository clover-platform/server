package plus.xyc.server.i18n.entry.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "添加词条评论请求")
public class EntryCommentAddRequest {

    @Schema(description = "词条ID", hidden = true)
    private Long entryId;
    @Schema(description = "评论内容")
    private String content;
    @Schema(description = "语言")
    private String language;
    @Schema(description = "创建用户ID", hidden = true)
    private Long createUserId;

}
