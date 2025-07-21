package plus.xyc.server.i18n.entry.entity.response;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import plus.xyc.server.i18n.file.entity.dto.File;

@Data
@Schema(description = "词条")
public class EntryResponse implements Serializable {
    
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "文件")
    private File file;

    @Schema(description = "键")
    private String identifier;

    @Schema(description = "值")
    private String value;

    @Schema(description = "上下文描述")
    private String context;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "创建人")
    private Long createUserId;

    @Schema(description = "更新人")
    private Long updateUserId;
    
}
