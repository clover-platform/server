package plus.xyc.server.wiki.collect.entity.dto;

import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 收藏夹
 * </p>
 *
 * @author generator
 * @since 2025-04-22
 */
@Data
@Accessors(chain = true)
@Schema(name = "Collect", description = "收藏夹")
public class Collect implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "页面ID")
    private Long pageId;

    @Schema(description = "收藏时间")
    private Date createTime;

    @Schema(description = "知识库ID")
    private Long bookId;
}
