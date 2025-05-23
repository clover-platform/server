package plus.xyc.server.i18n.entry.mapper;

import org.apache.ibatis.annotations.Param;
import plus.xyc.server.i18n.entry.entity.dto.EntryComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.i18n.entry.entity.request.EntryCommentListRequest;

import java.util.List;

/**
 * <p>
 * 词条评论 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface EntryCommentMapper extends BaseMapper<EntryComment> {

    List<EntryComment> query(@Param("request") EntryCommentListRequest request);
    EntryComment getLatestComment(@Param("entryId") Long entryId, @Param("userId") Long userId, @Param("language") String language);

}
