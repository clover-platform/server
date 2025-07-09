package plus.xyc.server.i18n.member.service;

import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.member.entity.dto.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.member.entity.request.MemberListRequest;
import plus.xyc.server.i18n.member.entity.response.MemberResponse;

import java.util.List;

/**
 * <p>
 * 成员 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface MemberService extends IService<Member> {

    void addModuleOwner(Long moduleId, Long accountId);
    List<MemberResponse> findMembers(Long moduleId);
    PageResult<MemberResponse> query(PageRequest page, MemberListRequest request);
    void delete(Long userId, List<Long> moduleIds);

}
