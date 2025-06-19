package plus.xyc.server.i18n.file.service;

import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.file.entity.dto.FileRevisionCommit;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 变更详情 服务类
 * </p>
 *
 * @author generator
 * @since 2025-06-17
 */
public interface FileRevisionCommitService extends IService<FileRevisionCommit> {

    void add(Long revisionId, List<Long> entryIds);
    void delete(Long revisionId, List<Long> entries);
    void update(Long revisionId, List<Entry> entries, List<Entry> origin);

}
