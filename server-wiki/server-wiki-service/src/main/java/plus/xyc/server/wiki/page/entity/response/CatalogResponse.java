package plus.xyc.server.wiki.page.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "目录")
public class CatalogResponse {

    @Schema(description = "自增ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "所属知识库")
    private Long bookId;

    @Schema(description = "上级目录")
    private Long parentId;

    @Schema(description = "创建者")
    private Long owner;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "子元素")
    private List<CatalogResponse> children;

    @Schema(description = "是否收藏")
    private Boolean collected;


}
