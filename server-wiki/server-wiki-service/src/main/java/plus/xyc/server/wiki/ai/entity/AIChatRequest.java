package plus.xyc.server.wiki.ai.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "聊天请求")
public class AIChatRequest {

    @Schema(description = "聊天内容")
    private String content;
    @Schema(description = "场景 chat=聊天 writer=写作")
    private String type;
    @Schema(description = "参考内容")
    private String data;

}
