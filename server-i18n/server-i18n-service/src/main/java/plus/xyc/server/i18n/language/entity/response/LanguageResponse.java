package plus.xyc.server.i18n.language.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "语言")
@Data
public class LanguageResponse {

    @Schema(description = "名称")
    private String name;
    @Schema(description = "编码")
    private String code;
    @Schema(description = "ID")
    private Long id;

}
