package plus.xyc.server.i18n.entry.service;

import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.entry.entity.request.EntryAIResultRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryResultListRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryResultSaveRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryResultResponse;

import java.util.List;

/**
 * <p>
 * 翻译结果 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface EntryResultService extends IService<EntryResult> {

    List<EntryResult> getLastResults(List<Long> ids, String language);
    List<EntryResult> getResults(List<Long> ids, String language);
    PageResult<EntryResultResponse> query(PageQueryRequest page, EntryResultListRequest request);
    void saveResult(EntryResultSaveRequest request);
    void delete(Long id, Long userId);
    void approve(Long id, Long userId);
    List<String> ai(EntryAIResultRequest request);
    void removeApproval(Long id, Long userId);

}
