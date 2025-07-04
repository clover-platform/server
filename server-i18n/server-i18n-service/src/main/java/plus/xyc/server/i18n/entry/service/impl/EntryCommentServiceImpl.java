package plus.xyc.server.i18n.entry.service.impl;

import com.github.pagehelper.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.entry.entity.dto.EntryComment;
import plus.xyc.server.i18n.entry.entity.mapstruct.EntryCommentMapStruct;
import plus.xyc.server.i18n.entry.entity.request.EntryCommentAddRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCommentListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCommentResponse;
import plus.xyc.server.i18n.entry.mapper.EntryCommentMapper;
import plus.xyc.server.i18n.entry.mapper.EntryMapper;
import plus.xyc.server.i18n.entry.service.EntryCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.common.enums.I18nCode;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.service.MainAccountApiService;

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

    @DubboReference
    private MainAccountApiService mainAccountApiService;
    @Resource
    private EntryCommentMapStruct mapStruct;
    @Resource
    private EntryMapper entryMapper;

    @Override
    public PageResult<EntryCommentResponse> query(PageRequest page, EntryCommentListRequest request) {
        try(Page<EntryComment> pageResult = page.start()) {
            List<EntryComment> list = baseMapper.query(request);

            List<Long> userIds = list.stream().map(EntryComment::getCreateUserId).filter(Objects::nonNull).toList();

            ApiAccountListRequest apiRequest = new ApiAccountListRequest();
            apiRequest.setSize(userIds.size());
            apiRequest.setIds(userIds);
            PageResult<ApiAccountResponse> r =  mainAccountApiService.list(apiRequest);
            List<EntryCommentResponse> responses = list.stream().map(entryComment -> {
                EntryCommentResponse response = mapStruct.toEntryCommentResponse(entryComment);
                response.setUser(r.getData().stream().filter(apiAccountResponse -> apiAccountResponse.getId().equals(entryComment.getCreateUserId())).findFirst().orElse(null));
                return response;
            }).toList();

            return PageResult.of(pageResult.getTotal(), responses);
        }
    }

    @Override
    @Transactional
    @DistributedLock(value = "i18n:entry:comment:add", el = false)
    public void add(EntryCommentAddRequest request) {
        EntryComment latestComment = getLatestComment(request.getEntryId(), request.getCreateUserId(), request.getLanguage());
        if (latestComment != null) {
            long now = System.currentTimeMillis();
            long createTime = latestComment.getCreateTime().getTime();
            long interval = now - createTime;
            if (interval < 5000) {
                throw new ResultException(I18nCode.ENTRY_COMMENT_FAST.code, MessageUtils.get(I18nCode.ENTRY_COMMENT_FAST.key));
            }
        }

        EntryComment comment = new EntryComment();
        comment.setEntryId(request.getEntryId());
        comment.setContent(request.getContent());
        comment.setLanguage(request.getLanguage());
        comment.setCreateUserId(request.getCreateUserId());
        save(comment);
    }

    @Override
    public EntryComment getLatestComment(Long entryId, Long createUserId, String language) {
        return baseMapper.getLatestComment(entryId, createUserId, language);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long id) {
        EntryComment comment = getById(id);
        if(!comment.getCreateUserId().equals(userId)) {
            throw new ResultException(I18nCode.ACCESS_ERROR.code, MessageUtils.get(I18nCode.ACCESS_ERROR.key));
        }
        removeById(id);
    }
}
