package plus.xyc.server.i18n.member.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 邀请记录
 * </p>
 *
 * @author generator
 * @since 2025-06-30
 */
@Data
@Accessors(chain = true)
@TableName("member_invite")
@Schema(name = "MemberInvite", description = "邀请记录")
public class MemberInvite implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "创建人")
    private Long creatorId;

    @Schema(description = "授权角色")
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private Object roles;
}
