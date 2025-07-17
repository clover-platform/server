package plus.xyc.server.i18n.activity.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityResponse extends org.zkit.support.server.message.api.entity.response.ActivityResponse {
    
    @Schema(description = "用户信息")
    private ApiAccountResponse account;

}
