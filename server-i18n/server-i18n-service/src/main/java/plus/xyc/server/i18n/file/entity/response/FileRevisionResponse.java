package plus.xyc.server.i18n.file.entity.response;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(name = "FileRevisionResponse", description = "文件变更")
public class FileRevisionResponse {
    
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "创建时间")
    private Date createTime;

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

    @Schema(description = "版本号")
    private Integer version;

}
