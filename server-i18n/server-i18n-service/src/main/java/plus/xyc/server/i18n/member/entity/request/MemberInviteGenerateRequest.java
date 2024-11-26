package plus.xyc.server.i18n.member.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "生成邀请链接")
public class MemberInviteGenerateRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;
    @Schema(description = "角色")
    private List<Integer> roles;
    @Schema(description = "用户ID", hidden = true)
    private Long userId;

}
