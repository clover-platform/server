package plus.xyc.server.main.api.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "查询请求")
public class AccountRequest {

    @Schema(description = "ID列表")
    private List<Long> ids;
    @Schema(description = "请求数量")
    private Integer size;
    @Schema(description = "页面")
    private Integer page;

}
