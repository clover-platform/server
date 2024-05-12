package plus.xyc.server.main.account.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户信息")
public class AccountProfileResponse {

    @Schema(description = "权限")
    private List<String> authorities;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "当前团队id")
    private Long currentTeamId;

    @Schema(description = "当前项目id")
    private Long currentProjectId;

}
