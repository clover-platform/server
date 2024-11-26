package plus.xyc.server.i18n.bundle.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "Bundle", description = "文件包")
public class BundleCreateRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "源文件匹配规则")
    private List<String> sources;

    @Schema(description = "是否包含源文件")
    private Boolean includeSource;

    @Schema(description = "导出配置")
    private ExportConfig export;

    @Data
    @Schema(name = "ExportConfig", description = "导出配置")
    public static class ExportConfig {

        @Schema(description = "导出格式")
        private String format;

        @Schema(description = "导出配置")
        private Object config;

    }

}