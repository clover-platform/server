package plus.xyc.server.main.activity.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 动态
 * </p>
 *
 * @author generator
 * @since 2025-06-30
 */
@Data
@Accessors(chain = true)
@Schema(name = "Activity", description = "动态")
public class Activity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "富文本内容")
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private Object content;

    @Schema(description = "应用ID")
    private String app;

    @Schema(description = "地址")
    private String url;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "动态时间")
    private Date createTime;

    @Schema(description = "元数据")
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private Object metadata;
}
