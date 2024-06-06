package plus.xyc.server.i18n.member.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.member.entity.dto.Member;
import plus.xyc.server.i18n.member.entity.dto.MemberRole;
import plus.xyc.server.i18n.member.entity.enums.MemberRoleType;
import plus.xyc.server.i18n.member.entity.mapstruct.MemberMapStruct;
import plus.xyc.server.i18n.member.entity.request.MemberListRequest;
import plus.xyc.server.i18n.member.entity.response.MemberResponse;
import plus.xyc.server.i18n.member.mapper.MemberMapper;
import plus.xyc.server.i18n.member.service.MemberRoleService;
import plus.xyc.server.i18n.member.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.module.service.ModuleAccessService;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.rest.MainAccountRestApi;

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
    @Resource
    private ModuleAccessService moduleAccessService;
    @Resource
    private MainAccountRestApi mainAccountRestApi;

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
        return toResponse(members);
    }

    private List<MemberResponse> toResponse(List<Member> members) {
        List<Long> memberIds = members.stream().map(Member::getId).toList();
        List<MemberRole> roles = memberRoleService.findByMemberIds(memberIds);
        Result<PageResult<ApiAccountResponse>> result = mainAccountRestApi.list(memberIds, memberIds.size(), 1);
        return members.stream().map(member -> {
            MemberResponse response = memberMapStruct.toMemberResponse(member);
            List<Integer> memberRoles = roles.stream()
                    .filter(role -> role.getMemberId().equals(member.getId()))
                    .map(MemberRole::getRole)
                    .toList();
            response.setRoles(memberRoles);
            if(result.isSuccess()) {
                List<ApiAccountResponse> accounts = result.getData().getData();
                response.setUser(accounts.stream()
                        .filter(account -> account.getId().equals(member.getAccountId()))
                        .findFirst()
                        .orElse(null));
            }
            return response;
        }).toList();
    }

    @Override
    public PageResult<MemberResponse> query(PageQueryRequest pageRequest, MemberListRequest request) {
        if(!moduleAccessService.isMember(request.getModuleId(), request.getUserId()))
            return PageResult.of(0, List.of());
        Page<Member> page = pageRequest.toPage();
        List<Member> members = baseMapper.query(page, request);
        return PageResult.of(page.getTotal(), toResponse(members));
    }
}
