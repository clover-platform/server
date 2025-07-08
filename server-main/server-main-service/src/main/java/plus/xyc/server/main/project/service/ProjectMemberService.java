package plus.xyc.server.main.project.service;

import plus.xyc.server.main.project.entity.dto.ProjectMember;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 项目成员 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
public interface ProjectMemberService extends IService<ProjectMember> {

    void leave(Long id, Long userId);
    void leave(List<Long> ids, Long userId);
    List<Long> findJoinedProjectIds(Long userId, List<Long> projectIds);

}
