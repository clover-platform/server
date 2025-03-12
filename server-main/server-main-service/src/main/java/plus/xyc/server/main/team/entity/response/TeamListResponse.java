package plus.xyc.server.main.team.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

import java.util.Date;

@Data
@Schema(description = "列表查询的团队信息")
public class TeamListResponse {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "团队名称")
    private String name;

    @Schema(description = "团队封面")
    private String cover;

    @Schema(description = "创建人")
    private Long ownerId;

    @Schema(description = "所有者")
    private ApiAccountResponse owner;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "唯一标识")
    private String teamKey;

    @Schema(description = "所属成员类型")
    private Integer memberType;

    @Schema(description = "是否收藏")
    private Boolean isCollect;

}
