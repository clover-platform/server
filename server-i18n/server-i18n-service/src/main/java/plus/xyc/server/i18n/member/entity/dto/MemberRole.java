package plus.xyc.server.i18n.member.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 成员角色
 * </p>
 *
 * @author generator
 * @since 2025-06-19
 */
@Data
@Accessors(chain = true)
@TableName("member_role")
@Schema(name = "MemberRole", description = "成员角色")
public class MemberRole implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long memberId;

    @Schema(description = "角色 0:查看 1:管理员 2:创建者 3:翻译 4:校验")
    private Integer role;
}
