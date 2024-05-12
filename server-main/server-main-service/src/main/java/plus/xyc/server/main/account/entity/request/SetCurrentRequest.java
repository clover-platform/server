package plus.xyc.server.main.account.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetCurrentRequest {

    private Long accountId;
    private Long teamId;
    private Long projectId;

}
