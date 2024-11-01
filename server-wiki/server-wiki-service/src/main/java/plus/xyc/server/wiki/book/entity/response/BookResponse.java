package plus.xyc.server.wiki.book.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.wiki.book.entity.dto.BookHomePage;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "查询知识库响应")
public class BookResponse {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "知识库名称")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "所属项目")
    private Long projectId;

    @Schema(description = "封面")
    private String cover;

    @Schema(description = "LOGO")
    private String logo;

    @Schema(description = "创建者")
    private Long ownerId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "隐私性 0-公开；1-知识库成员可见；2-项目成员可见；3-团队成员可见")
    private Integer privacy;

    @Schema(description = "访问路径")
    private String path;

    @Schema(description = "主页内容")
    private BookHomePage homePage;

    @Schema(description = "统计信息")
    private BookCountResponse count;

    @Schema(description = "成员列表")
    private List<ApiAccountResponse> members;

}
