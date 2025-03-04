package plus.xyc.server.main.project.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.team.entity.dto.Team;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "项目信息")
public class ProjectResponse extends Project {

    @Schema(description = "创建人")
    private ApiAccountResponse owner;

    @Schema(description = "所属团队")
    private Team team;

    @Schema(description = "是否收藏")
    private Boolean isCollect;

    @Schema(description = "所属成员类型")
    private Integer memberType;

}
