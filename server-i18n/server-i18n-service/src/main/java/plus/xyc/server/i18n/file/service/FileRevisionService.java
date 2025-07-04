package plus.xyc.server.i18n.file.service;

import plus.xyc.server.i18n.entry.entity.request.EntryRequest;
import plus.xyc.server.i18n.file.entity.dto.FileRevision;
import plus.xyc.server.i18n.file.entity.response.FileRevisionResponse;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 变更 服务类
 * </p>
 *
 * @author generator
 * @since 2025-06-17
 */
public interface FileRevisionService extends IService<FileRevision> {

    void add(Long moduleId, Long fileId, Long userId, String url, List<EntryRequest> entries);
    List<FileRevision> findListByFileIds(List<Long> fileIds);
    FileRevision findCurrentByFileId(Long fileId);
    List<FileRevisionResponse> list(Long fileId);

}
