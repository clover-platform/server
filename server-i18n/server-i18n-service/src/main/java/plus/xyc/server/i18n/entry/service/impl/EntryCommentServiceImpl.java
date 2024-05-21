package plus.xyc.server.i18n.entry.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.entry.entity.dto.EntryComment;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.entity.mapstruct.EntryCommentMapStruct;
import plus.xyc.server.i18n.entry.entity.request.EntryCommentListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCommentResponse;
import plus.xyc.server.i18n.entry.mapper.EntryCommentMapper;
import plus.xyc.server.i18n.entry.service.EntryCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.rest.MainAccountRestApi;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 词条评论 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
public class EntryCommentServiceImpl extends ServiceImpl<EntryCommentMapper, EntryComment> implements EntryCommentService {

    @Resource
    private MainAccountRestApi mainAccountRestApi;
    @Resource
    private EntryCommentMapStruct mapStruct;

    @Override
    public PageResult<EntryCommentResponse> query(PageQueryRequest page, EntryCommentListRequest request) {
        Page<EntryComment> pageResult = page.toPage();
        List<EntryComment> list = baseMapper.query(pageResult, request);

        List<Long> userIds = list.stream().map(EntryComment::getCreateUserId).filter(Objects::nonNull).toList();
        Result<PageResult<ApiAccountResponse>> r =  mainAccountRestApi.list(userIds, userIds.size(), 1);
        if(!r.isSuccess()) {
            throw ResultException.internal();
        }
        List<EntryCommentResponse> responses = list.stream().map(entryComment -> {
            EntryCommentResponse response = mapStruct.toEntryCommentResponse(entryComment);
            response.setUser(r.getData().getData().stream().filter(apiAccountResponse -> apiAccountResponse.getId().equals(entryComment.getCreateUserId())).findFirst().orElse(null));
            return response;
        }).toList();

        return PageResult.of(pageResult.getTotal(), responses);
    }
}
