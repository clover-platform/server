package plus.xyc.server.main.project.entity.dto;

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
 * 收藏的项目
 * </p>
 *
 * @author generator
 * @since 2025-03-01
 */
@Data
@Accessors(chain = true)
@TableName("project_collect")
@Schema(name = "ProjectCollect", description = "收藏的项目")
public class ProjectCollect implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "收藏时间")
    private Date createTime;
}
