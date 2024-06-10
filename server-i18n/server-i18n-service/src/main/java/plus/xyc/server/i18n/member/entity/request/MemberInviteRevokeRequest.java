package plus.xyc.server.i18n.member.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "撤销邀请链接")
public class MemberInviteRevokeRequest {

    @Schema(description = "模块ID")
    private Long moduleId;
    @Schema(description = "邀请链接ID")
    private Long id;
    private List<Integer> roles;
    @Schema(description = "用户ID", hidden = true)
    private Long userId;

}
