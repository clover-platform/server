package plus.xyc.server.i18n.entry.service;

import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.entry.entity.dto.EntryComment;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.entry.entity.request.EntryCommentAddRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCommentListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCommentResponse;

/**
 * <p>
 * 词条评论 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface EntryCommentService extends IService<EntryComment> {

    PageResult<EntryCommentResponse> query(PageRequest page, EntryCommentListRequest request);
    void add(EntryCommentAddRequest request);
    EntryComment getLatestComment(Long entryId, Long createUserId, String language);
    void delete(Long userId, Long id);

}
