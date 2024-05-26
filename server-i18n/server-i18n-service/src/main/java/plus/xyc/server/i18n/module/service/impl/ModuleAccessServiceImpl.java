package plus.xyc.server.i18n.module.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.member.entity.dto.MemberRole;
import plus.xyc.server.i18n.member.mapper.MemberRoleMapper;
import plus.xyc.server.i18n.module.service.ModuleAccessService;

import java.util.List;

@Service
@Slf4j
public class ModuleAccessServiceImpl implements ModuleAccessService {

    @Resource
    private MemberRoleMapper memberRoleMapper;

    @Override
    public boolean check(Long moduleId, Long userId, List<Integer> needRoles) {
        List<MemberRole> memberRoles = memberRoleMapper.findByModuleIdAndAccountId(moduleId, userId);
        List<Integer> roles = memberRoles.stream().map(MemberRole::getRole).toList();
        for (Integer role : needRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }
}
