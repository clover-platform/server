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
 * 翻译结果
 * </p>
 *
 * @author generator
 * @since 2024-05-25
 */
@Data
@TableName("entry_result")
@Schema(name = "EntryResult", description = "翻译结果")
public class EntryResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "词条ID")
    private Long entryId;

    @Schema(description = "翻译结果")
    private String content;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "翻译人")
    private Long translatorId;

    @Schema(description = "更新人")
    private Long checkerId;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "是否已经确认")
    private Boolean verified;

    @Schema(description = "语言")
    private String language;

    @Schema(description = "是否已删除")
    private Boolean deleted;

    @Schema(description = "批准时间")
    private Date verifiedTime;
}
