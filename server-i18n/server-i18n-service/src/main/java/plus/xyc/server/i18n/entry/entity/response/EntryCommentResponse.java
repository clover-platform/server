package plus.xyc.server.i18n.entry.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import plus.xyc.server.i18n.entry.entity.dto.EntryComment;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "词条评论")
public class EntryCommentResponse extends EntryComment {

    @Schema(description = "评论人")
    private ApiAccountResponse user;

}
