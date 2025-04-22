package plus.xyc.server.qa.api.entity.dto;

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
 * 
 * </p>
 *
 * @author generator
 * @since 2025-04-22
 */
@SuppressWarnings("unused")
@Data
@Accessors(chain = true)
@TableName("api_group")
@Schema(name = "ApiGroup", description = "")
public class ApiGroup implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "分组名称")
    private String name;

    @Schema(description = "唯一标识")
    private String label;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "所属项目")
    private Long projectId;

    @Schema(description = "创建人")
    private Long createUserId;

    @Schema(description = "创建时间")
    private Date createTime;
}
