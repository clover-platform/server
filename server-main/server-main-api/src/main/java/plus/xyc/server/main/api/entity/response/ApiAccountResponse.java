package plus.xyc.server.main.api.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "ApiAccountResponse", description = "用户信息")
public class ApiAccountResponse implements Serializable {


    @Schema(description = "ID")
    private Long id;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "是否已删除")
    private Boolean deleted;

    @Schema(description = "是否启用")
    private Boolean enable;

    @Schema(description = "当前团队id")
    private Long currentTeamId;

    @Schema(description = "是否已激活")
    private Boolean active;

}
