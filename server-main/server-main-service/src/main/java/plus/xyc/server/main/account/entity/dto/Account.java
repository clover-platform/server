package plus.xyc.server.main.account.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author generator
 * @since 2025-03-01
 */
@Data
@Accessors(chain = true)
@Schema(name = "Account", description = "用户信息")
public class Account implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
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
}
