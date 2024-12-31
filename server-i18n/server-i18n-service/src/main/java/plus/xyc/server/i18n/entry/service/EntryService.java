package plus.xyc.server.i18n.entry.service;

import com.alibaba.fastjson2.JSONObject;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryCreateRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryEditRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCountResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithResultResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithStateResponse;
import plus.xyc.server.i18n.open.entity.request.OpenEntryPullRequest;
import plus.xyc.server.i18n.open.entity.request.OpenEntryPushRequest;

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

    PageResult<EntryWithStateResponse> query(PageRequest page, EntryListRequest request);
    PageResult<EntryWithStateResponse> all(EntryListRequest request);
    void sync(EntryListRequest request);
    EntryCountResponse count(EntryCountRequest request);
    List<EntryWithResultResponse> getEntryByBranchIdWithResult(Long branchId);
    void cloneEntriesBySourceId(Long sourceId, Long targetId);
    void cloneEntries(List<EntryWithResultResponse> sources, Long targetId);
    void create(EntryCreateRequest request);
    void edit(EntryEditRequest request);
    EntryWithStateResponse detail(Long id, String language);
    void remove(Long id, Long userId);
    List<Entry> getByBranchId(Long branchId);
    Entry findById(Long id);
    int countByBranchId(Long branchId);
    List<Long> findIdByBranchId(Long branchId);
    void push(OpenEntryPushRequest request);
    JSONObject pull(OpenEntryPullRequest request);

}
