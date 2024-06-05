package plus.xyc.server.wiki.catalog.entity.dto;

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
 * 目录
 * </p>
 *
 * @author generator
 * @since 2024-06-05
 */
@Data
@Schema(name = "Catalog", description = "目录")
public class Catalog implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "自增ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "目录名称")
    private String name;

    @Schema(description = "所属项目")
    private Long projectId;

    @Schema(description = "上级目录")
    private Long parentId;

    @Schema(description = "创建者")
    private Long owner;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "是否已删除")
    private Boolean deleted;
}
