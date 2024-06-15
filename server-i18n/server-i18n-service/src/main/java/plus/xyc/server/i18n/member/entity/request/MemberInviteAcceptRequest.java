package plus.xyc.server.i18n.member.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "生成邀请链接")
public class MemberInviteAcceptRequest {

    @Schema(description = "邀请码")
    private String token;
    @Schema(description = "用户ID", hidden = true)
    private Long id;

}
