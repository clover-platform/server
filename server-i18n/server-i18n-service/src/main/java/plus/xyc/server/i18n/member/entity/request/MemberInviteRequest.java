package plus.xyc.server.i18n.member.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "邀请链接查询")
public class MemberInviteRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;

}
