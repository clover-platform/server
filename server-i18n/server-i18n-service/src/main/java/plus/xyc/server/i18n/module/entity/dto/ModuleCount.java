package plus.xyc.server.i18n.module.entity.dto;

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
 * 模块统计
 * </p>
 *
 * @author generator
 * @since 2025-04-22
 */
@SuppressWarnings("unused")
@Data
@Accessors(chain = true)
@TableName("module_count")
@Schema(name = "ModuleCount", description = "模块统计")
public class ModuleCount implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "语言代码")
    private String code;

    @Schema(description = "分支ID")
    private Long branchId;

    @Schema(description = "词条数")
    private Long totalEntry;

    @Schema(description = "已翻译词条")
    private Long translatedEntry;

    @Schema(description = "已校验词条")
    private Long verifiedEntry;

    @Schema(description = "字数")
    private Long totalEntryLength;
}
