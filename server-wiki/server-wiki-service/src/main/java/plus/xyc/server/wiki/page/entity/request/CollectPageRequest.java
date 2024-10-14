package plus.xyc.server.wiki.page.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "收藏页面")
public class CollectPageRequest {

    @Schema(description = "知识库ID")
    private Long bookId;

    @Schema(description = "页面ID")
    private Long id;

    @Schema(description = "是否收藏")
    private Boolean collect;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

}
