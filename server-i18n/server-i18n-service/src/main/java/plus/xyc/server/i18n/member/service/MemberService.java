package plus.xyc.server.i18n.member.service;

import plus.xyc.server.i18n.member.entity.dto.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 成员 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface MemberService extends IService<Member> {

    void addModuleOwner(Long userId, Long moduleId);

}
