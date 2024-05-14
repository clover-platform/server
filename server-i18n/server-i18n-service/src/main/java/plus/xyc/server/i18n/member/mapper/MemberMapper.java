package plus.xyc.server.i18n.member.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.i18n.member.entity.dto.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 成员 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface MemberMapper extends BaseMapper<Member> {

    List<Member> findByModuleId(@Param("moduleId") Long moduleId);

}
