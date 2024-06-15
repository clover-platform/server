package plus.xyc.server.i18n.member.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import plus.xyc.server.i18n.module.entity.response.ModuleLanguageResponse;

import java.util.List;

@Data
@Schema(description = "邀请详情")
public class MemberInviteDetailResponse {

    @Schema(description = "唯一标识")
    private String identifier;
    @Schema(description = "模块名称")
    private String name;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "源语言")
    private String source;
    @Schema(description = "角色")
    private List<Long> roles;
    @Schema(description = "目标语言")
    private List<ModuleLanguageResponse> targets;

}
