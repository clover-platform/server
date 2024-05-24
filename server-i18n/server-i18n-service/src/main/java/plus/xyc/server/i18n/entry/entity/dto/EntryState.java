package plus.xyc.server.i18n.entry.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 词条翻译状态
 * </p>
 *
 * @author generator
 * @since 2024-05-24
 */
@Data
@TableName("entry_state")
@Schema(name = "EntryState", description = "词条翻译状态")
public class EntryState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "词条ID")
    private Long entryId;

    @Schema(description = "语言")
    private String language;

    @Schema(description = "是否已翻译")
    private Boolean translated;

    @Schema(description = "翻译结果ID")
    private Long resultId;

    @Schema(description = "是否已校验")
    private Boolean verified;

    @Schema(description = "翻译时间")
    private Date translationTime;

    @Schema(description = "确认的时间")
    private Date verificationTime;
}