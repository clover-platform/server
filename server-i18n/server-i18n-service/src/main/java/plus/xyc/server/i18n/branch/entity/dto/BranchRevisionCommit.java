package plus.xyc.server.i18n.branch.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 变更详情
 * </p>
 *
 * @author generator
 * @since 2024-11-29
 */
@Data
@Accessors(chain = true)
@TableName("branch_revision_commit")
@Schema(name = "BranchRevisionCommit", description = "变更详情")
public class BranchRevisionCommit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "变更ID")
    private Long revisionId;

    @Schema(description = "操作 1 新增 2 删除 3 变更")
    private Integer action;

    @Schema(description = "词条ID")
    private Long entryId;

    @Schema(description = "原始内容")
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private Object origin;

    @Schema(description = "当前内容")
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private Object current;
}
