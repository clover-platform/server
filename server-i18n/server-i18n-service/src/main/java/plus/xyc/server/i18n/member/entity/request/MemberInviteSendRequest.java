package plus.xyc.server.i18n.member.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "邀请链接查询")
public class MemberInviteSendRequest {

    @Schema(description = "模块ID")
    private Long moduleId;
    @Schema(description = "角色")
    private List<Integer> roles;
    @Schema(description = "用户ID", hidden = true)
    private Long userId;
    @Schema(description = "邮箱 , 分割")
    private String emails;
    @Schema(description = "内容")
    private String content;

}
