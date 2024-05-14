package plus.xyc.server.i18n.module.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import plus.xyc.server.i18n.member.entity.response.MemberResponse;

import java.util.List;

@Data
@Schema(description = "概览")
public class ModuleDashboardResponse {

    @Schema(description = "语言")
    private List<ModuleLanguageResponse> languages;
    @Schema(description = "详情")
    private ModuleResponse detail;
    @Schema(description = "统计")
    private ModuleCountResponse count;
    @Schema(description = "成员")
    private List<MemberResponse> members;

}
