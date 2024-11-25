package plus.xyc.server.i18n.member.mapper;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.i18n.activity.entity.dto.Activity;
import plus.xyc.server.i18n.activity.entity.request.ActivityListRequest;
import plus.xyc.server.i18n.member.entity.dto.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.i18n.member.entity.request.MemberListRequest;

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
    int countByModuleIdAndAccountId(@Param("moduleId") Long moduleId, @Param("accountId") Long accountId);
    List<Member> query(@Param("request") MemberListRequest request);

}
