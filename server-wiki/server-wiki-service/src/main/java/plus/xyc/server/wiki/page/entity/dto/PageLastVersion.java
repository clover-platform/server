package plus.xyc.server.wiki.page.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 最新版本
 * </p>
 *
 * @author generator
 * @since 2025-08-01
 */
@Data
@Accessors(chain = true)
@TableName("page_last_version")
@Schema(name = "PageLastVersion", description = "最新版本")
public class PageLastVersion implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "页面ID")
    private Long pageId;

    @Schema(description = "版本号")
    private Long versionNumber;
}
