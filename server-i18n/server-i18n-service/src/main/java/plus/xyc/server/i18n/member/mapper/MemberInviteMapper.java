package plus.xyc.server.i18n.member.mapper;

import org.apache.ibatis.annotations.Param;
import plus.xyc.server.i18n.member.entity.dto.MemberInvite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.i18n.member.entity.request.MemberInviteRequest;

import java.util.List;

/**
 * <p>
 * 邀请记录 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface MemberInviteMapper extends BaseMapper<MemberInvite> {

    List<MemberInvite> query(@Param("request") MemberInviteRequest request);

}
