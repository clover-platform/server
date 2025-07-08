package plus.xyc.server.main.team.service;

import plus.xyc.server.main.team.entity.dto.TeamMember;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 团队成员 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
public interface TeamMemberService extends IService<TeamMember> {

    void leave(Long id, Long userId);

}
