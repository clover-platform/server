package plus.xyc.server.main.project.service.impl;

import plus.xyc.server.main.project.entity.dto.ProjectMember;
import plus.xyc.server.main.project.mapper.ProjectMemberMapper;
import plus.xyc.server.main.project.service.ProjectMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目成员 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
@Service
public class ProjectMemberServiceImpl extends ServiceImpl<ProjectMemberMapper, ProjectMember> implements ProjectMemberService {

    @Override
    @Transactional
    public void leave(Long id, Long userId) {
        lambdaUpdate().eq(ProjectMember::getProjectId, id).eq(ProjectMember::getAccountId, userId).remove();
    }

    @Override
    @Transactional
    public void leave(List<Long> ids, Long userId) {
        if(ids.isEmpty()) {
            return;
        }
        lambdaUpdate().in(ProjectMember::getProjectId, ids).eq(ProjectMember::getAccountId, userId).remove();
    }

    @Override
    public List<Long> findJoinedProjectIds(Long userId, List<Long> projectIds) {
        if(projectIds.isEmpty()) {
            return List.of();
        }
        return lambdaQuery().in(ProjectMember::getProjectId, projectIds).eq(ProjectMember::getAccountId, userId).list().stream().map(ProjectMember::getProjectId).toList();
    }

}
