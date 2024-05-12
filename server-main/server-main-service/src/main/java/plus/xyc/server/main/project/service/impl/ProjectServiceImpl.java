package plus.xyc.server.main.project.service.impl;

import org.springframework.cache.annotation.Cacheable;
import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.project.mapper.ProjectMapper;
import plus.xyc.server.main.project.service.ProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 项目 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Override
    @Cacheable(value = "account:projects", key = "#userId")
    public List<Project> my(Long userId) {
        return baseMapper.findMy(userId);
    }
}
