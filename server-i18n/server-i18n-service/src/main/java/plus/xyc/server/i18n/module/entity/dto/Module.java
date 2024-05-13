package plus.xyc.server.i18n.module.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2024-05-13
 */
@Data
@Schema(name = "Module", description = "项目")
public class Module implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "唯一标识")
    private String identifier;

    @Schema(description = "模块名称")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "所属项目")
    private Long projectId;

    @Schema(description = "创建人")
    private Long owner;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "源语言")
    private String source;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "是否已删除")
    private Boolean deleted;
}