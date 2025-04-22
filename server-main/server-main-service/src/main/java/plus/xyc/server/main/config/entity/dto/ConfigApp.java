package plus.xyc.server.main.config.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用
 * </p>
 *
 * @author generator
 * @since 2025-04-22
 */
@SuppressWarnings("unused")
@Data
@Accessors(chain = true)
@TableName("config_app")
@Schema(name = "ConfigApp", description = "应用")
public class ConfigApp implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "唯一标识")
    private String appId;

    @Schema(description = "名称key")
    private String nameKey;

    @Schema(description = "描述Key")
    private String descriptionKey;

    @Schema(description = "地址")
    private String href;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "图片图标")
    private String iconUrl;

    @Schema(description = "是否启用")
    private Boolean enable;

    @Schema(description = "排序")
    private Integer sort;
}
