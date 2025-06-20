package plus.xyc.server.i18n.entry.entity.dto;

import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 词条
 * </p>
 *
 * @author generator
 * @since 2025-06-20
 */
@Data
@Accessors(chain = true)
@Schema(name = "Entry", description = "词条")
public class Entry implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "键")
    private String identifier;

    @Schema(description = "值")
    private String value;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "创建人")
    private Long createUserId;

    @Schema(description = "更新人")
    private Long updateUserId;

    @Schema(description = "是否已删除")
    private Boolean deleted;

    @Schema(description = "上下文描述")
    private String context;
}
