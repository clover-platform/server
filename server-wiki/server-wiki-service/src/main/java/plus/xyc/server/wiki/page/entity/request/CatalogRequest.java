package plus.xyc.server.wiki.page.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "请求目录")
public class CatalogRequest {

    @Schema(description = "知识库ID")
    private Long bookId;

}
