package plus.xyc.server.wiki.page.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "修改父级目录")
public class CatalogParentRequest {

    @Schema(description = "知识库ID", hidden = true)
    private Long bookId;
    @Schema(description = "页面ID", hidden = true)
    private Long id;
    @Schema(description = "父级ID")
    private Long parentId;

}
