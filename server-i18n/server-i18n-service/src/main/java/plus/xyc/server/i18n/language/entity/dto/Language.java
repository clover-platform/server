package plus.xyc.server.i18n.language.entity.dto;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 语言
 * </p>
 *
 * @author generator
 * @since 2025-06-30
 */
@Data
@Accessors(chain = true)
@Schema(name = "Language", description = "语言")
public class Language implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "语言代码")
    private String code;
}
