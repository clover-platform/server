package plus.xyc.server.i18n.bundle.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 文件包
 * </p>
 *
 * @author generator
 * @since 2024-11-28
 */
@Data
@Schema(name = "Bundle", description = "文件包")
public class Bundle implements Serializable {
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

    @Schema(description = "文件名")
    private String name;

    @Schema(description = "目标格式")
    private String format;

    @Schema(description = "是否包含源语言")
    private Boolean includeSource;

    @Schema(description = "配置")
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private Object formatConfig;

    @Schema(description = "内容")
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private Object sources;
}
