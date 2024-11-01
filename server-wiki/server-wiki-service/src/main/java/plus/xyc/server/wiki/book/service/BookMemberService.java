package plus.xyc.server.wiki.book.service;

import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.wiki.book.entity.dto.BookMember;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
public interface BookMemberService extends IService<BookMember> {

    List<ApiAccountResponse> getMemberList(Long bookId);

}
