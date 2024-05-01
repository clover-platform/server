package plus.xyc.server.main.project.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author generator
 * @since 2024-05-02
 */
@Getter
@Setter
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 创建人
     */
    private Integer ownerId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 团队ID
     */
    private Integer teamId;
}
