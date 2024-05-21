package plus.xyc.server.i18n.entry.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "词条")
public class EntryWithResultResponse extends Entry {

    @Schema(description = "词条翻译结果")
    private List<EntryResult> results;

}
