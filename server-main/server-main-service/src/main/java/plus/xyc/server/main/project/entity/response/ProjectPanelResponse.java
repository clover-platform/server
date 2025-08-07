package plus.xyc.server.main.project.entity.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "项目面板信息")
public class ProjectPanelResponse {

    @Schema(description = "项目列表")
    private List<ProjectResponse> projects;

    @Schema(description = "最近项目")
    private List<ProjectResponse> recents;

    @Schema(description = "收藏项目")
    private List<ProjectResponse> collects;

}
