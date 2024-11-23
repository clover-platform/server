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
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
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
        if(members.isEmpty()) {
            return List.of();
        }

        List<Long> memberIds = members.stream().map(Member::getId).toList();
        List<Long> accountIds = members.stream().map(Member::getAccountId).toList();
        List<MemberRole> roles = memberRoleService.findByMemberIds(memberIds);
        ApiAccountListRequest apiRequest = new ApiAccountListRequest();
        apiRequest.setIds(accountIds);
        apiRequest.setSize(accountIds.size());
        apiRequest.setPage(1);
        Result<PageResult<ApiAccountResponse>> result = mainAccountRestApi.list(apiRequest);
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
        Page<Member> page = pageRequest.toPage();
        List<Member> members = baseMapper.query(page, request);
        if(members.isEmpty()) {
            return PageResult.of(0, List.of());
        }

        List<Long> memberIds = members.stream().map(Member::getId).toList();
        List<MemberRole> roles = memberRoleService.findByMemberIds(memberIds);

        List<Long> accountIds = members.stream().map(Member::getAccountId).toList();
        ApiAccountListRequest apiRequest = new ApiAccountListRequest();
        apiRequest.setIds(accountIds);
        apiRequest.setSize(accountIds.size());
        apiRequest.setPage(1);
        apiRequest.setKeyword(pageRequest.getKeyword());
        Result<PageResult<ApiAccountResponse>> result = mainAccountRestApi.list(apiRequest);

        if(!result.isSuccess())
            return PageResult.of(0, List.of());

        List<MemberResponse> responses = result.getData().getData().stream().map(account -> {
            Member member = members.stream()
                    .filter(m -> m.getAccountId().equals(account.getId()))
                    .findFirst()
                    .orElse(null);
            if(member == null)
                return null;
            MemberResponse response = memberMapStruct.toMemberResponse(member);
            List<Integer> memberRoles = roles.stream()
                    .filter(role -> role.getMemberId().equals(member.getId()))
                    .map(MemberRole::getRole)
                    .toList();
            response.setRoles(memberRoles);
            response.setUser(account);
            return response;
        }).toList();

        return PageResult.of(responses.size(), responses);
    }
}
