package plus.xyc.server.i18n.entry.entity.dto;

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
 * 词条
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Data
@Schema(name = "Entry", description = "词条")
public class Entry implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "分支ID")
    private Long branchId;

    @Schema(description = "键")
    private String key;

    @Schema(description = "值")
    private String value;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "创建人")
    private Long createUserId;

    @Schema(description = "更新人")
    private Long updateUserId;

    @Schema(description = "是否已删除")
    private Boolean deleted;
}