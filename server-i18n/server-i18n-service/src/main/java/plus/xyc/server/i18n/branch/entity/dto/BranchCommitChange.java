package plus.xyc.server.i18n.branch.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 变更详情
 * </p>
 *
 * @author generator
 * @since 2024-11-28
 */
@Data
@TableName("branch_commit_change")
@Schema(name = "BranchCommitChange", description = "变更详情")
public class BranchCommitChange implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "提交ID")
    private Long commitId;

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
