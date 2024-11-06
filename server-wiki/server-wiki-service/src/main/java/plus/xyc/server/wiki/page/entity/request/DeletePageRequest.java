package plus.xyc.server.wiki.page.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除页面")
public class DeletePageRequest {

    @Schema(description = "上级文档")
    private Long parent;

    @Schema(description = "知识库ID", hidden = true)
    private Long bookId;

    @Schema(description = "页面ID", hidden = true)
    private Long pageId;

    @Schema(description = "当前用户ID", hidden = true)
    private Long userId;

}
