package plus.xyc.server.i18n.branch.entity.dto;

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
 * 变更
 * </p>
 *
 * @author generator
 * @since 2024-11-29
 */
@Data
@Accessors(chain = true)
@TableName("branch_revision")
@Schema(name = "BranchRevision", description = "变更")
public class BranchRevision implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "分支ID")
    private Long branchId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "创建人")
    private Long createUser;

    @Schema(description = "是否是当前")
    private Boolean current;

    @Schema(description = "备注信息")
    private String message;

    @Schema(description = "新增数量")
    private Integer addedSize;

    @Schema(description = "更新数量")
    private Integer updatedSize;

    @Schema(description = "删除数量")
    private Integer deletedSize;
}
