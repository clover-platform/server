package plus.xyc.server.main.project.service;

import plus.xyc.server.main.project.entity.dto.Project;
import com.baomidou.mybatisplus.extension.service.IService;

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

    List<Project> my(Long userId);

}
