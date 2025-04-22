package plus.xyc.server.wiki.book.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 知识库成员
 * </p>
 *
 * @author generator
 * @since 2025-04-22
 */
@Data
@Accessors(chain = true)
@TableName("book_member")
@Schema(name = "BookMember", description = "知识库成员")
public class BookMember implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "知识库ID")
    private Long bookId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "角色 1-创建者；2-贡献者；3-只读")
    private Integer role;
}
