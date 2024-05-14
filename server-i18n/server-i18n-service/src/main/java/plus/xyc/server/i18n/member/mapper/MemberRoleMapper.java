package plus.xyc.server.i18n.member.mapper;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.i18n.member.entity.dto.MemberRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface MemberRoleMapper extends BaseMapper<MemberRole> {

    List<MemberRole> findByMemberIdIn(@Param("memberIdList") Collection<Long> memberIdList);

}
