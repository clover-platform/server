package plus.xyc.server.i18n.branch.entity.dto;

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
 * @since 2025-04-22
 */
@Data
@Accessors(chain = true)
@Schema(name = "Branch", description = "分支")
public class Branch implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "分支名")
    private String name;

    @Schema(description = "是否默认")
    private Boolean isDefault;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "是否已删除")
    private Boolean deleted;

    @Schema(description = "类型 0-empty 1-clone")
    private Integer type;
}
