package plus.xyc.server.main.api.entity.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@Schema(description = "项目事件")
public class ProjectEvent implements Serializable {

    public static final String EVENT_TYPE_JOIN = "join";
    public static final String EVENT_TYPE_LEAVE = "leave";
    public static final String EVENT_TYPE_UPDATE = "update";
    public static final String EVENT_TYPE_DELETE = "delete";
    public static final String EVENT_TYPE_CREATE = "create";

    @Schema(description = "事件类型")
    private String eventType;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "项目ID")
    private Long projectId;
    @Schema(description = "团队ID")
    private Long teamId;

}
