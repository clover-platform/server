package plus.xyc.server.i18n.module.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "模块查询请求")
public class ModuleQueryRequest {

    @Schema(description = "类型")
    private String type;
    @Schema(description = "用户名", hidden = true)
    private Long userId;

}
