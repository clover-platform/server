package plus.xyc.server.wiki.page.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新内容")
public class SavePageContentRequest {

    @Schema(description = "文章ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "HTML 内容")
    private String html;

    @Schema(description = "更新人", hidden = true)
    private Long updateUser;

    @Schema(description = "是否新版本")
    private Boolean newVersion;

}
