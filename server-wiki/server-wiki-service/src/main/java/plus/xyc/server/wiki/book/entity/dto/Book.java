package plus.xyc.server.wiki.book.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 知识库
 * </p>
 *
 * @author generator
 * @since 2024-10-31
 */
@Data
@Schema(name = "Book", description = "知识库")
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "知识库名称")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "所属项目")
    private Long projectId;

    @Schema(description = "封面")
    private String cover;

    @Schema(description = "LOGO")
    private String logo;

    @Schema(description = "创建者")
    private Long ownerId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "是否已删除")
    private Boolean deleted;

    @Schema(description = "隐私性 0-公开；1-知识库成员可见；2-项目成员可见；3-团队成员可见")
    private Integer privacy;

    @Schema(description = "访问路径")
    private String path;
}
