package plus.xyc.server.wiki.book.service.impl;

import jakarta.annotation.Resource;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.rest.MainAccountRestApi;
import plus.xyc.server.wiki.book.entity.dto.BookMember;
import plus.xyc.server.wiki.book.mapper.BookMemberMapper;
import plus.xyc.server.wiki.book.service.BookMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
@Service
public class BookMemberServiceImpl extends ServiceImpl<BookMemberMapper, BookMember> implements BookMemberService {

    @Resource
    private MainAccountRestApi mainAccountRestApi;

    @Override
    public List<ApiAccountResponse> getMemberList(Long bookId) {
        List<BookMember> members = baseMapper.findUserIdByBookId(bookId);
        List<Long> ids = members.stream().map(BookMember::getUserId).collect(Collectors.toList());
        ApiAccountListRequest request = new ApiAccountListRequest();
        request.setIds(ids);
        request.setSize(ids.size());
        Result<PageResult<ApiAccountResponse>> result = mainAccountRestApi.list(request);
        if(!result.isSuccess()) {
            throw ResultException.internal();
        }
        return result.getData().getData();
    }
}
