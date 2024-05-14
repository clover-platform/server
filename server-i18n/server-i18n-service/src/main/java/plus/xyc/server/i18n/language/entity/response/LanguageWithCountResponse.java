package plus.xyc.server.i18n.language.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "语言统计数据")
public class LanguageWithCountResponse extends LanguageResponse {

    @Schema(description = "总数")
    private Integer total;
    @Schema(description = "已翻译")
    private Integer translated;
    @Schema(description = "已校对")
    private Integer verified;

}
