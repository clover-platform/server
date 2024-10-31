package plus.xyc.server.wiki.page.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 收藏夹
 * </p>
 *
 * @author generator
 * @since 2024-10-31
 */
@Data
@TableName("page_collect")
@Schema(name = "PageCollect", description = "收藏夹")
public class PageCollect implements Serializable {
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
