package plus.xyc.server.main.api.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "用户列表请求")
public class ApiAccountListRequest implements Serializable {

    @Schema(description = "用户ID列表")
    private List<Long> ids;
    @Schema(description = "关键字")
    private String keyword;
    @Schema(description = "每页大小")
    private Integer size = 100;
    @Schema(description = "页码")
    private Integer page = 1;

}
