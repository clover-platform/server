package plus.xyc.server.i18n.file.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文件变更
 * </p>
 *
 * @author generator
 * @since 2025-08-01
 */
@Data
@Accessors(chain = true)
@TableName("file_revision")
@Schema(name = "FileRevision", description = "文件变更")
public class FileRevision implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "文件ID")
    private Long fileId;

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

    @Schema(description = "文件地址")
    private String fileUrl;

    @Schema(description = "版本号")
    private Integer version;
}
