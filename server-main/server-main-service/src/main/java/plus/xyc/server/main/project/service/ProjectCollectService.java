package plus.xyc.server.main.project.service;

import plus.xyc.server.main.project.entity.dto.ProjectCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.main.team.entity.dto.TeamCollect;

import java.util.List;

/**
 * <p>
 * 收藏的项目 服务类
 * </p>
 *
 * @author generator
 * @since 2025-03-01
 */
public interface ProjectCollectService extends IService<ProjectCollect> {

    List<ProjectCollect> findByProjectIdsAndUserId(List<Long> projectIds, Long userId);

}
