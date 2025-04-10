package plus.xyc.server.wiki.book.service.impl;

import jakarta.annotation.Resource;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.service.MainAccountApiService;
import plus.xyc.server.wiki.book.entity.dto.BookMember;
import plus.xyc.server.wiki.book.mapper.BookMemberMapper;
import plus.xyc.server.wiki.book.service.BookMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.dubbo.config.annotation.DubboReference;
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

    @DubboReference
    private MainAccountApiService mainAccountApiService;

    @Override
    public List<ApiAccountResponse> getMemberList(Long bookId) {
        List<BookMember> members = baseMapper.findUserIdByBookId(bookId);
        List<Long> ids = members.stream().map(BookMember::getUserId).collect(Collectors.toList());
        ApiAccountListRequest request = new ApiAccountListRequest();
        request.setIds(ids);
        request.setSize(ids.size());
        PageResult<ApiAccountResponse> result = mainAccountApiService.list(request);
        return result.getData();
    }
}
