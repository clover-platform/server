package plus.xyc.server.main.project.service;

import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.main.api.entity.request.JoinProjectRequest;
import plus.xyc.server.main.project.entity.dto.Project;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.main.project.entity.request.ProjectListRequest;

import java.util.List;

/**
 * <p>
 * 项目 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
public interface ProjectService extends IService<Project> {

    List<Project> my(Long userId, Long teamId);
    boolean join(JoinProjectRequest request);
    PageResult<Project> list(PageRequest page, ProjectListRequest request);

}
