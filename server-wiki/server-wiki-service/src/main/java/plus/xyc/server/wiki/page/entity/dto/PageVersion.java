package plus.xyc.server.wiki.page.entity.dto;

import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
@Data
@TableName("page_version")
@Schema(name = "PageVersion", description = "")
public class PageVersion implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "页面ID")
    private Long pageId;

    @Schema(description = "版本号")
    private Long versionNumber;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "更新人")
    private Long updateUser;

    @Schema(description = "是否是当前")
    private Boolean current;
}
