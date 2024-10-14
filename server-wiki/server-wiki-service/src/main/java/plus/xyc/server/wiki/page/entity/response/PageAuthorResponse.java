package plus.xyc.server.wiki.page.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

@Data
@Schema(description = "文章作者")
public class PageAuthorResponse {

    @Schema(description = "用户信息")
    private ApiAccountResponse account;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "所有者")
    private Boolean owner = false;

}
