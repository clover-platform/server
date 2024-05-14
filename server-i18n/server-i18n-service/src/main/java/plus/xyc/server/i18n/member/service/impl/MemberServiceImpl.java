package plus.xyc.server.i18n.member.service.impl;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import plus.xyc.server.i18n.member.entity.dto.Member;
import plus.xyc.server.i18n.member.entity.dto.MemberRole;
import plus.xyc.server.i18n.member.entity.enums.MemberRoleType;
import plus.xyc.server.i18n.member.entity.mapstruct.MemberMapStruct;
import plus.xyc.server.i18n.member.entity.response.MemberResponse;
import plus.xyc.server.i18n.member.mapper.MemberMapper;
import plus.xyc.server.i18n.member.service.MemberRoleService;
import plus.xyc.server.i18n.member.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Resource
    private MemberMapStruct memberMapStruct;

    @Override
    @Transactional
    public void addModuleOwner(Long moduleId, Long accountId) {
        Member member = new Member();
        member.setAccountId(accountId);
        member.setModuleId(moduleId);
        save(member);
        MemberRole role = new MemberRole();
        role.setMemberId(member.getId());
        role.setRole(MemberRoleType.OWNER.code);
        memberRoleService.save(role);
    }

    @Override
    public List<MemberResponse> findMembers(Long moduleId) {
        List<Member> members = baseMapper.findByModuleId(moduleId);
        List<Long> memberIds = members.stream().map(Member::getId).toList();
        List<MemberRole> roles = memberRoleService.findByMemberIds(memberIds);
        return members.stream().map(member -> {
            MemberResponse response = memberMapStruct.toMemberResponse(member);
            List<Integer> memberRoles = roles.stream()
                    .filter(role -> role.getMemberId().equals(member.getId()))
                    .map(MemberRole::getRole)
                    .toList();
            response.setRoles(memberRoles);
            return response;
        }).toList();
    }
}
