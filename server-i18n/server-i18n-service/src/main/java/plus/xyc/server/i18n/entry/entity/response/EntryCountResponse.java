package plus.xyc.server.i18n.entry.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "词条数量统计响应")
public class EntryCountResponse {

    @Schema(description = "总数")
    private Long total;
    @Schema(description = "已翻译")
    private Long translated;
    @Schema(description = "已校对")
    private Long verified;

}
