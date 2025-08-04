package plus.xyc.server.main.activity.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 最近活动日志
 * </p>
 *
 * @author generator
 * @since 2025-08-04
 */
@Data
@Accessors(chain = true)
@TableName("activity_log")
@Schema(name = "ActivityLog", description = "最近活动日志")
public class ActivityLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "用户 ID")
    private Long userId;

    @Schema(description = "记录类型 1-项目 2-任务")
    private Integer action;

    @Schema(description = "关联业务ID")
    private Long itemId;

    @Schema(description = "活动时间")
    private Date time;
}
