package plus.xyc.server.main.account.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Data
@Schema(name = "Account", description = "用户信息")
public class Account implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
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

    @Schema(description = "当前项目id")
    private Long currentProjectId;

    @Schema(description = "是否已激活")
    private Boolean active;
}
