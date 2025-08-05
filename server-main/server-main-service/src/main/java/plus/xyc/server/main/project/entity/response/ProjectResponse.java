package plus.xyc.server.main.project.entity.response;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.team.entity.dto.Team;

@Data
@Schema(description = "项目信息")
public class ProjectResponse implements Serializable {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "项目名称")
    private String name;

    @Schema(description = "创建人")
    private Long ownerId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "团队ID")
    private Long teamId;

    @Schema(description = "唯一标识")
    private String projectKey;

    @Schema(description = "封面")
    private String cover;

    @Schema(description = "创建人")
    private ApiAccountResponse owner;

    @Schema(description = "所属团队")
    private Team team;

    @Schema(description = "是否收藏")
    private Boolean isCollect;

    @Schema(description = "所属成员类型")
    private Integer memberType;

    @Schema(description = "最后活动时间")
    private Date lastActivityTime;

}
