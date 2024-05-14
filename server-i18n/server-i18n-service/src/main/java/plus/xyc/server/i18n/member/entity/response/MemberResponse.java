package plus.xyc.server.i18n.member.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import plus.xyc.server.main.api.entity.response.AccountResponse;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "成员")
public class MemberResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "模块ID")
    private Long moduleId;
    @Schema(description = "成员ID")
    private Long accountId;
    @Schema(description = "加入时间")
    private Date joinTime;
    @Schema(description = "角色")
    private List<Integer> roles;
    @Schema(description = "用户")
    private AccountResponse user;

}
