package plus.xyc.server.i18n.module.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 目标语言
 * </p>
 *
 * @author generator
 * @since 2025-06-18
 */
@Data
@Accessors(chain = true)
@TableName("module_target_language")
@Schema(name = "ModuleTargetLanguage", description = "目标语言")
public class ModuleTargetLanguage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "团队ID")
    private Long moduleId;

    @Schema(description = "语言代码")
    private String code;
}
