package plus.xyc.server.main.team.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.main.account.entity.request.SetCurrentRequest;
import plus.xyc.server.main.account.service.AccountService;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.enums.MainCode;
import plus.xyc.server.main.project.entity.dto.Project;
import plus.xyc.server.main.project.service.ProjectService;
import plus.xyc.server.main.team.entity.dto.Team;
import plus.xyc.server.main.team.entity.dto.TeamCollect;
import plus.xyc.server.main.team.entity.dto.TeamMember;
import plus.xyc.server.main.team.entity.enums.TeamMemberType;
import plus.xyc.server.main.team.entity.request.CreateTeamRequest;
import plus.xyc.server.main.team.entity.request.InitTeamRequest;
import plus.xyc.server.main.team.entity.request.TeamListRequest;
import plus.xyc.server.main.team.entity.response.InitTeamResponse;
import plus.xyc.server.main.team.entity.response.TeamListResponse;
import plus.xyc.server.main.team.mapper.TeamMapper;
import plus.xyc.server.main.team.mapper.TeamMemberMapper;
import plus.xyc.server.main.team.service.TeamCollectService;
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
    @Resource
    private TeamCollectService teamCollectService;

    @Override
    @Cacheable(value = "account:teams#1d", key = "#userId")
    public List<TeamListResponse> my(Long userId) {
        return baseMapper.findMy(userId, null);
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
        project.setName(MessageUtils.get("default.project.name"));
        project.setOwnerId(request.getOwnerId());
        project.setProjectKey("default"+ team.getId());
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

    @Override
    @Transactional
    @DistributedLock({"'team'"})
    @CacheEvict(value = "account:teams#1d", key = "#request.ownerId")
    public void create(CreateTeamRequest request) {
        // 保存团队
        Team team = new Team();
        team.setCover(request.getCover());
        team.setName(request.getName());
        team.setOwnerId(request.getOwnerId());
        team.setTeamKey(request.getTeamKey());
        checkAndSave(team);

        // 保存项目信息
        Project project = new Project();
        project.setTeamId(team.getId());
        project.setName(MessageUtils.get("default.project.name"));
        project.setOwnerId(request.getOwnerId());
        project.setProjectKey("default"+ team.getId());
        projectService.checkAndSave(project);

        // 保存团队成员
        TeamMember teamMember = new TeamMember();
        teamMember.setTeamId(team.getId());
        teamMember.setAccountId(request.getOwnerId());
        teamMember.setType(TeamMemberType.OWNER.code);
        teamMemberMapper.insert(teamMember);
    }

    @Override
    public PageResult<TeamListResponse> list(PageRequest pr, TeamListRequest request) {
        try(Page<TeamListResponse> page = pr.start()) {
            String type = request.getType();
            switch (type) {
                case "all" -> baseMapper.findAllByUserId(request.getUserId(), pr.getKeyword());
                case "create" -> baseMapper.findMy(request.getUserId(), pr.getKeyword());
                case "join" -> baseMapper.findJoin(request.getUserId(), pr.getKeyword());
            }
            List<Long> teamIds = page.getResult().stream().map(TeamListResponse::getId).toList();
            List<TeamCollect> teamCollects = teamCollectService.findByTeamIdsAndUserId(teamIds, request.getUserId());
            List<Long> ownerIds = page.getResult().stream().map(TeamListResponse::getOwnerId).toList();
            ApiAccountListRequest apiAccountListRequest = new ApiAccountListRequest();
            apiAccountListRequest.setIds(ownerIds);
            PageResult<ApiAccountResponse> result = accountService.query(apiAccountListRequest);
            List<ApiAccountResponse> accounts = result.getData();
            page.getResult().forEach(item -> {
                item.setIsCollect(teamCollects.stream().anyMatch(collect -> collect.getTeamId().equals(item.getId())));
                item.setOwner(accounts.stream().filter(account -> account.getId().equals(item.getOwnerId())).findFirst().orElse(null));
            });
            return PageResult.of(page);
        }
    }

    @Override
    @CacheEvict(value = {"account:teams#1d", "account:projects#1d"}, key = "#userId")
    public void delete(Long id, Long userId) {
        int size = baseMapper.countByIdAndOwnerId(id, userId);
        if (size == 0) {
            throw new ResultException(MainCode.ACCESS_DENIED.code, MessageUtils.get(MainCode.ACCESS_DENIED.key));
        }
        UpdateWrapper<Team> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("deleted", true);
        update(updateWrapper);

        UpdateWrapper<Project> projectUpdateWrapper = new UpdateWrapper<>();
        projectUpdateWrapper.eq("team_id", id);
        projectUpdateWrapper.set("deleted", true);
        projectService.update(projectUpdateWrapper);
    }
}
