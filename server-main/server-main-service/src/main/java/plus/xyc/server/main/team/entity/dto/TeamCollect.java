package plus.xyc.server.main.team.entity.dto;

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
 * 收藏的团队
 * </p>
 *
 * @author generator
 * @since 2025-03-03
 */
@Data
@Accessors(chain = true)
@TableName("team_collect")
@Schema(name = "TeamCollect", description = "收藏的团队")
public class TeamCollect implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "团队ID")
    private Long teamId;

    @Schema(description = "收藏时间")
    private Date createTime;
}
