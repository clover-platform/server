package plus.xyc.server.i18n.file.entity.dto;

import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 分支
 * </p>
 *
 * @author generator
 * @since 2025-06-17
 */
@Data
@Accessors(chain = true)
@Schema(name = "File", description = "分支")
public class File implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "分支名")
    private String name;

    @Schema(description = "创建时间")
    private Date uploadTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "是否已删除")
    private Boolean deleted;

    @Schema(description = "上传人")
    private Long uploadUserId;

    @Schema(description = "更新人")
    private Long updateUserId;
}
