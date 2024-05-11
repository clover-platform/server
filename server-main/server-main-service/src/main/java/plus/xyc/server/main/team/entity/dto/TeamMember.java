package plus.xyc.server.main.team.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 团队成员
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
@Data
@TableName("team_member")
@Schema(name = "TeamMember", description = "团队成员")
public class TeamMember implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "团队ID")
    private Long teamId;

    @Schema(description = "成员ID")
    private Long accountId;

    @Schema(description = "加入时间")
    private Date joinTime;

    @Schema(description = "类型 0:普通成员 1:管理员 2:创建者")
    private Integer type;
}
