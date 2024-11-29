package plus.xyc.server.i18n.language.entity.dto;

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
 * 语言名称
 * </p>
 *
 * @author generator
 * @since 2024-11-29
 */
@Data
@Accessors(chain = true)
@TableName("language_name")
@Schema(name = "LanguageName", description = "语言名称")
public class LanguageName implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "语言")
    private String code;

    @Schema(description = "语言名称")
    private String name;

    @Schema(description = "语言ID")
    private Long languageId;
}
