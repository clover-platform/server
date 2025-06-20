package plus.xyc.server.i18n.activity.entity.dto;

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
 * 项目动态
 * </p>
 *
 * @author generator
 * @since 2025-06-20
 */
@Data
@Accessors(chain = true)
@Schema(name = "Activity", description = "项目动态")
public class Activity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "操作人")
    private Long userId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "操作 1:模块 2:文件 3:下载 4:成员 5:设置 6:词条")
    private Integer type;

    @Schema(description = "子操作 操作类型6时 1:词条 2:翻译 3:评论")
    private Integer subType;

    @Schema(description = "操作类型 1:增加 2:更新 3:删除")
    private Integer operate;

    @Schema(description = "操作内容")
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private Object detail;
}
