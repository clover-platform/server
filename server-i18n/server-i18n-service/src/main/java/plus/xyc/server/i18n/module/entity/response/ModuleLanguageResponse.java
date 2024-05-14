package plus.xyc.server.i18n.module.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import plus.xyc.server.i18n.language.entity.response.LanguageResponse;

@EqualsAndHashCode(callSuper = true)
@Schema(description = "语言")
@Data
public class ModuleLanguageResponse extends LanguageResponse {

    @Schema(description = "词条数")
    private Long totalEntry;
    @Schema(description = "已翻译词条")
    private Long translatedEntry;
    @Schema(description = "已校验词条")
    private Long verifiedEntry;

}
