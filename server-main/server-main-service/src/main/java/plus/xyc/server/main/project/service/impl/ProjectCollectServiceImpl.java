package plus.xyc.server.main.project.service.impl;

import plus.xyc.server.main.project.entity.dto.ProjectCollect;
import plus.xyc.server.main.project.mapper.ProjectCollectMapper;
import plus.xyc.server.main.project.service.ProjectCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.main.team.entity.dto.TeamCollect;

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

}
