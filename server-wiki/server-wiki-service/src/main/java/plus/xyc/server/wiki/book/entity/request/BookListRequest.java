package plus.xyc.server.wiki.book.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "知识库列表查询请求")
public class BookListRequest {

    @Schema(description = "用户ID", hidden = true)
    private Long userId;
    @Schema(description = "类型 all=全部；create=创建的；join=加入的")
    private String type;
    @Schema(description = "关键词", hidden = true)
    private String keyword;

}
