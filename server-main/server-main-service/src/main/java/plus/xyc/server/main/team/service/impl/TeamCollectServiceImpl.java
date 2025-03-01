package plus.xyc.server.main.team.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import plus.xyc.server.main.team.entity.dto.Team;
import plus.xyc.server.main.team.entity.dto.TeamCollect;
import plus.xyc.server.main.team.entity.request.TeamCollectRequest;
import plus.xyc.server.main.team.mapper.TeamCollectMapper;
import plus.xyc.server.main.team.service.TeamCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 收藏的团队 服务实现类
 * </p>
 *
 * @author generator
 * @since 2025-03-01
 */
@Service
public class TeamCollectServiceImpl extends ServiceImpl<TeamCollectMapper, TeamCollect> implements TeamCollectService {

    @Override
    @CacheEvict(value = "team:collect", key = "#request.userId")
    public void add(TeamCollectRequest request) {
        TeamCollect tc = new TeamCollect();
        tc.setTeamId(request.getId());
        tc.setUserId(request.getUserId());
        save(tc);
    }

    @Override
    @CacheEvict(value = "team:collect", key = "#request.userId")
    public void cancel(TeamCollectRequest request) {
        UpdateWrapper<TeamCollect> uw = new UpdateWrapper<>();
        uw.eq("team_id", request.getId());
        uw.eq("user_id", request.getUserId());
        remove(uw);
    }

    @Override
    @Cacheable(value = "team:collect", key = "#userId")
    public List<Team> my(Long userId) {
        return baseMapper.my(userId);
    }

    @Override
    public List<TeamCollect> findByTeamIdsAndUserId(List<Long> teamIds, Long userId) {
        if(teamIds != null && !teamIds.isEmpty()) {
            return baseMapper.findByTeamIdInAndUserId(teamIds, userId);
        }
        return List.of();
    }
}
