package plus.xyc.server.main.project.service.impl;

import com.github.pagehelper.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.main.account.entity.request.SetCurrentRequest;
import plus.xyc.server.main.account.service.AccountService;
import plus.xyc.server.main.api.entity.request.JoinProjectRequest;
import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.project.entity.dto.ProjectMember;
import plus.xyc.server.main.project.entity.request.ProjectListRequest;
import plus.xyc.server.main.project.mapper.ProjectMapper;
import plus.xyc.server.main.project.mapper.ProjectMemberMapper;
import plus.xyc.server.main.project.service.ProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.main.team.entity.dto.Team;
import plus.xyc.server.main.team.entity.dto.TeamMember;
import plus.xyc.server.main.team.mapper.TeamMapper;
import plus.xyc.server.main.team.mapper.TeamMemberMapper;

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
@Slf4j
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Resource
    private TeamMapper teamMapper;
    @Resource
    private TeamMemberMapper teamMemberMapper;
    @Resource
    private ProjectMemberMapper projectMemberMapper;
    @Resource
    private AccountService accountService;

    @Override
    @Cacheable(value = "account:projects#1d", key = "#userId")
    public List<Project> my(Long userId, Long teamId) {
        return baseMapper.findJoin(userId, teamId);
    }

    @Override
    @Transactional
    @DistributedLock("'project:member:' + #request.projectId")
    public boolean join(JoinProjectRequest request) {
        log.info("join project: {}", request);
        Project project = getById(request.getProjectId());
        Team team = teamMapper.selectById(project.getTeamId());
        // 先查询是否存在
        int tmSize = teamMemberMapper.countByAccountIdAndTeamId(request.getUserId(), team.getId());
        if(tmSize == 0) {
            TeamMember teamMember = new TeamMember();
            teamMember.setAccountId(request.getUserId());
            teamMember.setTeamId(team.getId());
            teamMember.setType(0);
            teamMemberMapper.insert(teamMember);
        }

        // 保存项目成员
        // 先查询是否存在
        int pmSize = projectMemberMapper.countByAccountIdAndProjectId(request.getUserId(), project.getId());
        if(pmSize == 0) {
            ProjectMember projectMember = new ProjectMember();
            projectMember.setAccountId(request.getUserId());
            projectMember.setProjectId(project.getId());
            projectMember.setType(0);
            projectMemberMapper.insert(projectMember);
        }
        SetCurrentRequest setCurrentRequest = new SetCurrentRequest();
        setCurrentRequest.setProjectId(project.getId());
        setCurrentRequest.setTeamId(team.getId());
        setCurrentRequest.setAccountId(request.getUserId());
        accountService.setCurrent(setCurrentRequest);
        return true;
    }

    @Override
    public PageResult<Project> list(PageRequest pr, ProjectListRequest request) {
        log.info("list page: {}", pr);
        log.info("list project: {}", request);
        try(Page<Project> page = pr.start()) {
            String type = request.getType();
            switch (type) {
                case "all" -> baseMapper.findAllByUserId(request.getUserId(), request.getTeamId());
                case "create" -> baseMapper.findMy(request.getUserId(), request.getTeamId());
                case "join" -> baseMapper.findJoin(request.getUserId(), request.getTeamId());
            };
            return PageResult.of(page);
        }
    }
}
