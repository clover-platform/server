package plus.xyc.server.main.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.project.entity.dto.ProjectCollect;
import plus.xyc.server.main.project.entity.request.ProjectCollectRequest;
import plus.xyc.server.main.project.mapper.ProjectCollectMapper;
import plus.xyc.server.main.project.service.ProjectCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 收藏的项目 服务实现类
 * </p>
 *
 * @author generator
 * @since 2025-03-01
 */
@Service
public class ProjectCollectServiceImpl extends ServiceImpl<ProjectCollectMapper, ProjectCollect> implements ProjectCollectService {

    @Override
    public List<ProjectCollect> findByProjectIdsAndUserId(List<Long> teamIds, Long userId) {
        if(teamIds != null && !teamIds.isEmpty()) {
            return baseMapper.findByProjectIdInAndUserId(teamIds, userId);
        }
        return List.of();
    }

    @Override
    @CacheEvict(value = "team:project", key = "#request.userId")
    public void add(ProjectCollectRequest request) {
        ProjectCollect pc = new ProjectCollect();
        pc.setProjectId(request.getId());
        pc.setUserId(request.getUserId());
        save(pc);
    }

    @Override
    @CacheEvict(value = "team:project", key = "#request.userId")
    public void cancel(ProjectCollectRequest request) {
        UpdateWrapper<ProjectCollect> uw = new UpdateWrapper<>();
        uw.eq("project_id", request.getId());
        uw.eq("user_id", request.getUserId());
        remove(uw);
    }

    @Override
    @Cacheable(value = "team:project", key = "#userId")
    public List<Project> my(Long userId) {
        return baseMapper.my(userId);
    }
}
