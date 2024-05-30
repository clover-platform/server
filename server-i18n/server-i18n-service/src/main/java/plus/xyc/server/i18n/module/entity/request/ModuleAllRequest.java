package plus.xyc.server.i18n.module.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "模块查询请求")
public class ModuleAllRequest {

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "用户名", hidden = true)
    private Long userId;

}
