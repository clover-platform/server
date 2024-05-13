package plus.xyc.server.i18n.member.service.impl;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import plus.xyc.server.i18n.member.entity.dto.Member;
import plus.xyc.server.i18n.member.entity.dto.MemberRole;
import plus.xyc.server.i18n.member.entity.enums.MemberRoleType;
import plus.xyc.server.i18n.member.mapper.MemberMapper;
import plus.xyc.server.i18n.member.service.MemberRoleService;
import plus.xyc.server.i18n.member.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 成员 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Resource
    private MemberRoleService memberRoleService;

    @Override
    @Transactional
    public void addModuleOwner(Long userId, Long moduleId) {
        Member member = new Member();
        member.setAccountId(userId);
        member.setModuleId(moduleId);
        save(member);
        MemberRole role = new MemberRole();
        role.setMemberId(member.getId());
        role.setRole(MemberRoleType.OWNER.code);
        memberRoleService.save(role);
    }
}
