package plus.xyc.server.wiki.book.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "保存首页")
public class BookHomePageSaveRequest {

    @Schema(description = "更新用户ID", hidden = true)
    private Long updateUserId;

    @Schema(description = "知识库ID", hidden = true)
    private Long bookId;

    @Schema(description = "访问路径", hidden = true)
    private String bookPath;

    @Schema(description = "内容")
    private String content;

}
