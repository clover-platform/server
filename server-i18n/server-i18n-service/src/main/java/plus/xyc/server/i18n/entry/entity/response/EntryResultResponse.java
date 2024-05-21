package plus.xyc.server.i18n.entry.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "词条翻译结果")
public class EntryResultResponse extends EntryResult {

    @Schema(description = "译者")
    private ApiAccountResponse translator;
    @Schema(description = "校对者")
    private ApiAccountResponse verifier;

}
