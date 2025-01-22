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
 * @since 2025-01-22
 */
@Data
@Accessors(chain = true)
@TableName("api_path")
@Schema(name = "ApiPath", description = "")
public class ApiPath implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "访问路径")
    private String path;

    @Schema(description = "请求方法")
    private String method;

    @Schema(description = "说明")
    private String summary;

    @Schema(description = "标签")
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private Object tags;

    @Schema(description = "类型")
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private Object consumes;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "创建人")
    private Long createUserId;

    @Schema(description = "更新人")
    private Long updateUserId;
}
