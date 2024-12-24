package plus.xyc.server.main.team.service.impl;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.main.account.entity.request.SetCurrentRequest;
import plus.xyc.server.main.account.service.AccountService;
import plus.xyc.server.main.enums.MainCode;
import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.project.service.ProjectService;
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
@Slf4j
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    @Resource
    private TeamMemberMapper teamMemberMapper;
    @Resource
    private AccountService accountService;
    @Resource
    private ProjectService projectService;

    @Override
    @Cacheable(value = "account:teams#1d", key = "#userId")
    public List<Team> my(Long userId) {
        return baseMapper.findMy(userId);
    }

    private void checkAndSave(Team team) {
        int size = baseMapper.countByTeamKeyAndDeleted(team.getTeamKey(), false);
        if (size > 0) {
            throw new ResultException(MainCode.TEAM_REPEATED.code, MessageUtils.get(MainCode.TEAM_REPEATED.key));
        }
        save(team);
    }

    @Override
    @Transactional
    @DistributedLock({"'team'", "'project'", "'account:' + #request.ownerId"})
    public InitTeamResponse init(InitTeamRequest request) {
        // 保存团队
        Team team = new Team();
        team.setName(request.getName());
        team.setOwnerId(request.getOwnerId());
        team.setTeamKey(request.getKey());
        checkAndSave(team);

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
        project.setProjectKey(request.getProjectKey());
        projectService.checkAndSave(project);

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
