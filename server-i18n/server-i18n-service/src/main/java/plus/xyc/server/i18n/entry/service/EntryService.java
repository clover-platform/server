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
import plus.xyc.server.i18n.entry.entity.request.EntryRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCountResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithResultResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithStateResponse;
import plus.xyc.server.i18n.entry.entity.response.UpdateEntriesResponse;
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
    EntryCountResponse count(EntryCountRequest request);
    List<EntryWithResultResponse> getEntryByFileIdWithResult(Long fileId);
    void create(EntryCreateRequest request);
    void edit(EntryEditRequest request);
    EntryWithStateResponse detail(Long id, String language);
    void remove(Long id, Long userId);
    List<Entry> getByFileId(Long fileId);
    Entry findById(Long id);
    int countByFileId(Long fileId);
    List<Long> findIdByFileId(Long fileId);
    void push(OpenEntryPushRequest request);
    JSONObject pull(OpenEntryPullRequest request);
    void deleteByIds(List<Long> ids);
    UpdateEntriesResponse updateEntries(Long moduleId, Long fileId, Long userId, List<EntryRequest> requestEntries);
    List<Entry> getByIds(List<Long> ids);

}
