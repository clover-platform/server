package plus.xyc.server.wiki.book.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "知识库统计信息")
public class BookCountResponse {

    @Schema(description = "成员数")
    private Integer memberCount;
    @Schema(description = "文章数")
    private Integer pageCount;
    @Schema(description = "字数")
    private Integer wordCount;

}
