package plus.xyc.server.main.team.entity.dto;

import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 团队
 * </p>
 *
 * @author generator
 * @since 2025-08-04
 */
@Data
@Accessors(chain = true)
@Schema(name = "Team", description = "团队")
public class Team implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "团队名称")
    private String name;

    @Schema(description = "创建人")
    private Long ownerId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "唯一标识")
    private String teamKey;

    @Schema(description = "是否已删除")
    private Boolean deleted;

    @Schema(description = "封面")
    private String cover;
}
