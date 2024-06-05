package plus.xyc.server.i18n.branch.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import plus.xyc.server.i18n.branch.entity.dto.Branch;
import plus.xyc.server.i18n.entry.entity.dto.Entry;

import java.util.List;

@Data
@Schema(description = "分支合并信息")
public class BranchMergeEntriesResponse {

    @Schema(description = "新增")
    private List<Entry> added;
    @Schema(description = "修改")
    private List<Entry> changed;
    @Schema(description = "删除")
    private List<Entry> deleted;
    @Schema(description = "合并")
    private List<Entry> merged;
    @Schema(description = "分支")
    private Branch branch;
    @Schema(description = "默认分支")
    private Branch defaultBranch;

}
