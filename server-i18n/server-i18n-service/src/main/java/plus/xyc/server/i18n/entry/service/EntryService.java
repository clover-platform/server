package plus.xyc.server.i18n.entry.service;

import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCreateRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryEditRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCountResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithResultResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithStateResponse;

import java.util.List;

/**
 * <p>
 * 词条 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface EntryService extends IService<Entry> {

    int wordCount(Long moduleId);
    PageResult<EntryResponse> query(PageQueryRequest page, EntryListRequest request);
    EntryCountResponse count(EntryCountRequest request);
    List<EntryWithResultResponse> getEntryByBranchIdWithResult(Long branchId);
    void cloneEntriesBySourceId(Long sourceId, Long targetId);
    void cloneEntries(List<EntryWithResultResponse> sources, Long targetId);
    void create(EntryCreateRequest request);
    List<Entry> getByModuleId(Long moduleId);
    void edit(EntryEditRequest request);
    EntryWithStateResponse detail(Long id, String language);
    void remove(Long id, Long userId);

}
