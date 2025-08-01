package plus.xyc.server.main.account.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "设置当前")
public class SetCurrentRequest {

    @Schema(description = "所有者", hidden = true)
    private Long accountId;

    @Schema(description = "团队ID")
    private Long teamId;

}
