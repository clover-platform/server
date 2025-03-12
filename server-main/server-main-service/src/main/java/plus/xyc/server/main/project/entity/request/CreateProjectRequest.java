package plus.xyc.server.main.project.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "创建项目请求")
public class CreateProjectRequest {

    @Schema(description = "项目名称")
    private String name;

    @Schema(description = "创建人", hidden = true)
    private Long ownerId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "团队ID")
    private Long teamId;

    @Schema(description = "唯一标识")
    private String projectKey;

    @Schema(description = "封面")
    private String cover;

}
