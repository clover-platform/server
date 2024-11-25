package plus.xyc.server.i18n.bundle.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "查询下载包")
public class BundleQueryRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;

}
