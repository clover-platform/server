package plus.xyc.server.i18n.member.service.impl;

import plus.xyc.server.i18n.member.entity.dto.MemberRole;
import plus.xyc.server.i18n.member.mapper.MemberRoleMapper;
import plus.xyc.server.i18n.member.service.MemberRoleService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
public class MemberRoleServiceImpl extends ServiceImpl<MemberRoleMapper, MemberRole> implements MemberRoleService {

    @Override
    public List<MemberRole> findByMemberIds(List<Long> memberId) {
        return baseMapper.findByMemberIdIn(memberId);
    }

    @Override
    public void delete(List<Long> memberIds) {
        if(memberIds.isEmpty())
            return;
        remove(new LambdaQueryWrapper<MemberRole>().in(MemberRole::getMemberId, memberIds));
    }
}
