package plus.xyc.server.wiki.book.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建知识库请求")
public class CreateBookRequest {

    @Schema(description = "拥有者", hidden = true)
    private Long ownerId;

    @Schema(description = "知识库名称")
    private String name;

    @Schema(description = "访问路径")
    private String path;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "封面")
    private String cover;

    @Schema(description = "LOGO")
    private String logo;

    @Schema(description = "隐私性 0-公开；1-知识库成员可见；2-项目成员可见；3-团队成员可见")
    private Integer privacy;

}
