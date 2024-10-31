package plus.xyc.server.wiki.page.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 目录
 * </p>
 *
 * @author generator
 * @since 2024-10-31
 */
@Data
@Schema(name = "Page", description = "目录")
public class Page implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "自增ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "所属知识库")
    private Long bookId;

    @Schema(description = "上级目录")
    private Long parentId;

    @Schema(description = "创建者")
    private Long owner;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "是否已删除")
    private Boolean deleted;
}
