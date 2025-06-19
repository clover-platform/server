package plus.xyc.server.i18n.file.entity.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Accessors(chain = true)
@Schema(name = "File", description = "文件")
public class FileResponse implements Serializable {
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "分支名")
    private String name;

    @Schema(description = "创建时间")
    private Date uploadTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "上传人")
    private Long uploadUserId;

    @Schema(description = "更新人")
    private Long updateUserId;

    @Schema(description = "是否已导入 0 未导入 1 已导入")
    private Integer importStatus;

    @Schema(description = "变更版本")
    private Integer revisionVersion;

    @Schema(description = "字数")
    private Integer wordCount;
}
