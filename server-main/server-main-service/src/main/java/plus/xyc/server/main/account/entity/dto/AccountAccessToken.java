package plus.xyc.server.main.account.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 个人令牌
 * </p>
 *
 * @author generator
 * @since 2025-08-04
 */
@Data
@Accessors(chain = true)
@TableName("account_access_token")
@Schema(name = "AccountAccessToken", description = "个人令牌")
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

    @Schema(description = "范围")
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private Object scopes;
}
