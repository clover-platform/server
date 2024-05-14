package plus.xyc.server.i18n.member.service;

import plus.xyc.server.i18n.member.entity.dto.MemberRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface MemberRoleService extends IService<MemberRole> {

    List<MemberRole> findByMemberIds(List<Long> memberId);

}
