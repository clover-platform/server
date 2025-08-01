package plus.xyc.server.i18n.module.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author generator
 * @since 2025-08-01
 */
@Data
@Accessors(chain = true)
@TableName("module_collect")
@Schema(name = "ModuleCollect", description = "")
public class ModuleCollect implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "创建时间")
    private Date createdTime;
}
