package plus.xyc.server.main.account.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;
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
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否已删除
     */
    private Boolean deleted;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 当前团队id
     */
    private Integer currentTeamId;

    /**
     * 当前项目id
     */
    private Integer currentProjectId;

    /**
     * 是否已激活
     */
    private Boolean active;
}
