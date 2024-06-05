package plus.xyc.server.i18n.entry.service;

import org.apache.ibatis.annotations.Param;
import plus.xyc.server.i18n.entry.entity.dto.EntryState;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;

/**
 * <p>
 * 词条翻译状态 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-24
 */
public interface EntryStateService extends IService<EntryState> {

    Long countTranslated(EntryCountRequest request);
    Long countVerified(EntryCountRequest request);
    void translate(Long entryId,String language, Long resultId);
    void removeTranslate(Long entryId,String language, Long resultId);
    void approve(Long entryId,String language, Long resultId);
    void removeApproval(Long entryId,String language, Long resultId);

}
