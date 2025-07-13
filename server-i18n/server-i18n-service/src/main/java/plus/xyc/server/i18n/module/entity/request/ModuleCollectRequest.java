package plus.xyc.server.i18n.module.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "模块收藏请求")
public class ModuleCollectRequest {

    @Schema(description = "模块ID")
    private Long id;

    @Schema(description = "用户名", hidden = true)
    private Long userId;
    
}
