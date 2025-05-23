package plus.xyc.server.wiki.page.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建页面")
public class CreatePageRequest {

    @Schema(description = "拥有者", hidden = true)
    private Long ownerId;

    @Schema(description = "上级文档")
    private Long parent;

    @Schema(description = "知识库ID", hidden = true)
    private Long bookId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

}
