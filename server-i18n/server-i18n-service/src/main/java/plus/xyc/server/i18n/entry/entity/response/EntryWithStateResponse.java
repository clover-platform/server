package plus.xyc.server.i18n.entry.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;

@EqualsAndHashCode(callSuper = true)
@Data
public class EntryWithStateResponse extends Entry {

    @Schema(description = "是否已翻译")
    private Boolean translated;
    @Schema(description = "是否已校验")
    private Boolean verified;
    @Schema(description = "翻译结果")
    private EntryResult translation;

}
