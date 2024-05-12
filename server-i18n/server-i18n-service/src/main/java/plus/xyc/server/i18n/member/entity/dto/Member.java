package plus.xyc.server.i18n.member.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 成员
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Data
@Schema(name = "Member", description = "成员")
public class Member implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "成员ID")
    private Long accountId;

    @Schema(description = "加入时间")
    private Date joinTime;

    @Schema(description = "类型 0:查看 1:管理员 2:创建者 3:翻译 4:校验")
    private Object roles;
}
