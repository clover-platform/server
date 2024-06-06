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
 * 词条评论
 * </p>
 *
 * @author generator
 * @since 2024-06-06
 */
@Data
@TableName("entry_comment")
@Schema(name = "EntryComment", description = "词条评论")
public class EntryComment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "词条ID")
    private Long entryId;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "评论人")
    private Long createUserId;

    @Schema(description = "语言")
    private String language;
}
