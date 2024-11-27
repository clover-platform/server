package plus.xyc.server.main.account.entity.dto;

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
 * 
 * </p>
 *
 * @author generator
 * @since 2024-11-27
 */
@Data
@TableName("account_access_token")
@Schema(name = "AccountAccessToken", description = "")
public class AccountAccessToken implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "账号ID")
    private Long accountId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "令牌")
    private String token;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "过期时间")
    private Date expirationTime;
}
