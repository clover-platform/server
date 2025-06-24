package plus.xyc.server.i18n.entry.entity.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import plus.xyc.server.i18n.entry.entity.dto.Entry;

@Data
@Schema(description = "更新结果")
public class UpdateEntriesResponse {

    @Schema(description = "新增词条")
    private List<Entry> newEntries;
    @Schema(description = "更新词条")
    private List<Entry> updateEntries;
    @Schema(description = "删除词条")
    private List<Entry> deleteEntries;
    @Schema(description = "原始词条")
    private List<Entry> originEntries;
    
}
