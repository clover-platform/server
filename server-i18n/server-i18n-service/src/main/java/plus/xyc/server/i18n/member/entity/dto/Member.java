package plus.xyc.server.i18n.member.entity.dto;

import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 成员
 * </p>
 *
 * @author generator
 * @since 2025-06-17
 */
@Data
@Accessors(chain = true)
@Schema(name = "Member", description = "成员")
public class Member implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "成员ID")
    private Long accountId;

    @Schema(description = "加入时间")
    private Date joinTime;
}
