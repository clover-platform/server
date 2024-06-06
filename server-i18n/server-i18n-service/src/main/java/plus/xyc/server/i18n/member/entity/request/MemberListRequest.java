package plus.xyc.server.i18n.member.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "成员")
public class MemberListRequest {

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "类型 all-全部,manager-管理员,translator-成员,proofreader-校对员")
    private String type;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

}
