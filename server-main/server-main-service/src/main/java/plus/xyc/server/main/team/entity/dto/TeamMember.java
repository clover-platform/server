package plus.xyc.server.main.team.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("team_member")
public class TeamMember implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 团队ID
     */
    private Integer teamId;

    /**
     * 成员ID
     */
    private Integer accountId;

    /**
     * 加入时间
     */
    private LocalDateTime joinTime;

    /**
     * 类型 0:普通成员 1:管理员 2:创建者
     */
    private Integer type;
}
