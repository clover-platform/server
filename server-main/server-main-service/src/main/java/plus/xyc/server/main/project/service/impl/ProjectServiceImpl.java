package plus.xyc.server.main.project.service.impl;

import com.github.pagehelper.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.main.account.entity.dto.Account;
import plus.xyc.server.main.account.entity.request.SetCurrentRequest;
import plus.xyc.server.main.account.service.AccountService;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.request.JoinProjectRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.enums.MainCode;
import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.project.entity.dto.ProjectCollect;
import plus.xyc.server.main.project.entity.dto.ProjectMember;
import plus.xyc.server.main.project.entity.enums.ProjectMemberType;
import plus.xyc.server.main.project.entity.request.ProjectListRequest;
import plus.xyc.server.main.project.entity.response.ProjectResponse;
import plus.xyc.server.main.project.mapper.ProjectMapper;
import plus.xyc.server.main.project.mapper.ProjectMemberMapper;
import plus.xyc.server.main.project.service.ProjectCollectService;
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
    @Resource
    private ProjectCollectService projectCollectService;

    @Override
    @Cacheable(value = "account:projects#1d", key = "#userId")
    public List<ProjectResponse> my(Long userId) {
        Account account = accountService.findById(userId);
        return baseMapper.findMy(userId, account.getCurrentTeamId(), null);
    }

    @Override
    public List<ProjectResponse> my(Long userId, Long teamId) {
        return baseMapper.findMy(userId, teamId, null);
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
    public PageResult<ProjectResponse> list(PageRequest pr, ProjectListRequest request) {
        log.info("list page: {}", pr);
        log.info("list project: {}", request);
        try(Page<ProjectResponse> page = pr.start()) {
            String type = request.getType();
            switch (type) {
                case "all" -> baseMapper.findAllByUserId(request.getUserId(), request.getTeamId(), pr.getKeyword());
                case "create" -> baseMapper.findMy(request.getUserId(), request.getTeamId(), pr.getKeyword());
                case "join" -> baseMapper.findJoin(request.getUserId(), request.getTeamId(), pr.getKeyword());
            }
            List<ProjectResponse> responses = page.getResult();
            List<Long> projectIds = responses.stream().map(ProjectResponse::getId).toList();
            List<ProjectCollect> projectCollects = projectCollectService.findByProjectIdsAndUserId(projectIds, request.getUserId());
            List<Long> ownerIds = responses.stream().map(ProjectResponse::getOwnerId).toList();
            ApiAccountListRequest apiAccountListRequest = new ApiAccountListRequest();
            apiAccountListRequest.setIds(ownerIds);
            PageResult<ApiAccountResponse> result = accountService.query(apiAccountListRequest);
            List<ApiAccountResponse> accounts = result.getData();
            List<Long> teamIds = responses.stream().map(ProjectResponse::getTeamId).toList();
            List<Team> teams;
            if(!teamIds.isEmpty()) {
                teams = teamMapper.selectBatchIds(teamIds);
            } else {
                teams = List.of();
            }
            responses.forEach(item -> {
                item.setIsCollect(projectCollects.stream().anyMatch(collect -> collect.getProjectId().equals(item.getId())));
                item.setOwner(accounts.stream().filter(account -> account.getId().equals(item.getOwnerId())).findFirst().orElse(null));
                item.setTeam(teams.stream().filter(team -> team.getId().equals(item.getTeamId())).findFirst().orElse(null));
            });
            return PageResult.of(page.getTotal(), responses);
        }
    }

    @Override
    public void checkAndSave(Project project) {
        int size = baseMapper.countByProjectKeyAndDeleted(project.getProjectKey(), false);
        if (size > 0) {
            throw new ResultException(MainCode.PROJECT_REPEATED.code, MessageUtils.get(MainCode.PROJECT_REPEATED.key));
        }
        save(project);
        // 保存项目成员
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(project.getId());
        projectMember.setAccountId(project.getOwnerId());
        projectMember.setType(ProjectMemberType.OWNER.code);
        projectMemberMapper.insert(projectMember);
    }
}
