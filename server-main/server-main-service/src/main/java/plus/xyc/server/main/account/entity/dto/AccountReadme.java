package plus.xyc.server.main.account.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 个人介绍
 * </p>
 *
 * @author generator
 * @since 2025-08-04
 */
@Data
@Accessors(chain = true)
@TableName("account_readme")
@Schema(name = "AccountReadme", description = "个人介绍")
public class AccountReadme implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键，账号ID")
    private Long id;

    @Schema(description = "内容")
    private String content;
}
