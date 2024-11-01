package plus.xyc.server.wiki.book.entity.dto;

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
 * 首页内容
 * </p>
 *
 * @author generator
 * @since 2024-11-01
 */
@Data
@TableName("book_home_page")
@Schema(name = "BookHomePage", description = "首页内容")
public class BookHomePage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "知识库ID")
    private Long bookId;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "更新人")
    private Long updateUser;

    @Schema(description = "更新时间")
    private Date updateTime;
}
