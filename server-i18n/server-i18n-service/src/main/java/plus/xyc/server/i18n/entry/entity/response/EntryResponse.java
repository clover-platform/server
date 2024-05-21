package plus.xyc.server.i18n.entry.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "词条响应")
public class EntryResponse extends Entry {

    @Schema(description = "翻译结果")
    private EntryResult translation;

}
