package plus.xyc.server.i18n.branch.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "分支")
public class BranchResponse {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "分支名")
    private String name;

    @Schema(description = "是否默认")
    private Boolean isDefault;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "类型 0-empty 1-clone")
    private Integer type;

}
