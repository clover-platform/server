package plus.xyc.server.main.team.service.impl;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import plus.xyc.server.main.account.entity.request.SetCurrentRequest;
import plus.xyc.server.main.account.service.AccountService;
import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.project.entity.dto.ProjectMember;
import plus.xyc.server.main.project.entity.enums.ProjectMemberType;
import plus.xyc.server.main.project.mapper.ProjectMapper;
import plus.xyc.server.main.project.mapper.ProjectMemberMapper;
import plus.xyc.server.main.team.entity.dto.Team;
import plus.xyc.server.main.team.entity.dto.TeamMember;
import plus.xyc.server.main.team.entity.enums.TeamMemberType;
import plus.xyc.server.main.team.entity.request.InitTeamRequest;
import plus.xyc.server.main.team.entity.response.InitTeamResponse;
import plus.xyc.server.main.team.mapper.TeamMapper;
import plus.xyc.server.main.team.mapper.TeamMemberMapper;
import plus.xyc.server.main.team.service.TeamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 团队 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    @Resource
    private TeamMemberMapper teamMemberMapper;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private ProjectMemberMapper projectMemberMapper;
    @Resource
    private AccountService accountService;

    @Override
    @Cacheable(value = "account:teams", key = "#userId")
    public List<Team> my(Long userId) {
        return baseMapper.findMy(userId);
    }

    @Override
    @Transactional
    public InitTeamResponse init(InitTeamRequest request) {
        // 保存团队
        Team team = new Team();
        team.setName(request.getName());
        team.setOwnerId(request.getOwnerId());
        save(team);

        // 保存团队成员
        TeamMember teamMember = new TeamMember();
        teamMember.setTeamId(team.getId());
        teamMember.setAccountId(request.getOwnerId());
        teamMember.setType(TeamMemberType.OWNER.code);
        teamMemberMapper.insert(teamMember);

        // 保存项目信息
        Project project = new Project();
        project.setTeamId(team.getId());
        project.setName(request.getProjectName());
        project.setOwnerId(request.getOwnerId());
        projectMapper.insert(project);

        // 保存项目成员
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(project.getId());
        projectMember.setAccountId(request.getOwnerId());
        projectMember.setType(ProjectMemberType.OWNER.code);
        projectMemberMapper.insert(projectMember);

        // 设置为当前
        SetCurrentRequest setCurrentRequest = new SetCurrentRequest(
                request.getOwnerId(),
                team.getId(),
                project.getId()
        );
        accountService.setCurrent(setCurrentRequest);

        InitTeamResponse response = new InitTeamResponse();
        response.setTeamId(team.getId());
        response.setProjectId(project.getId());
        return response;
    }
}
