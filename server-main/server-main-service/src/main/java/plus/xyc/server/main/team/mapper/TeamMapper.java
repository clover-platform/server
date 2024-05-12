package plus.xyc.server.main.team.mapper;

import plus.xyc.server.main.team.entity.dto.Team;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 团队 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-11
 */
public interface TeamMapper extends BaseMapper<Team> {

    List<Team> findMy(Long userId);

}
