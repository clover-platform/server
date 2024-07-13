package plus.xyc.server.main.project.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 项目
 * </p>
 *
 * @author generator
 * @since 2024-07-13
 */
@Data
@Schema(name = "Project", description = "项目")
public class Project implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "项目名称")
    private String name;

    @Schema(description = "创建人")
    private Long ownerId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "团队ID")
    private Long teamId;
}
