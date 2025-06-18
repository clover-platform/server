package plus.xyc.server.i18n.entry.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 词条评论
 * </p>
 *
 * @author generator
 * @since 2025-06-18
 */
@Data
@Accessors(chain = true)
@TableName("entry_comment")
@Schema(name = "EntryComment", description = "词条评论")
public class EntryComment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
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
