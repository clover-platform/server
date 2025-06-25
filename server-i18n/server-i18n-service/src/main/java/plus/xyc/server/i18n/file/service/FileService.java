package plus.xyc.server.i18n.file.service;

import plus.xyc.server.i18n.file.entity.dto.File;
import plus.xyc.server.i18n.file.entity.request.FileImportRequest;
import plus.xyc.server.i18n.file.entity.request.FileListRequest;
import plus.xyc.server.i18n.file.entity.request.FileRenameRequest;
import plus.xyc.server.i18n.file.entity.request.FileUploadRequest;
import plus.xyc.server.i18n.file.entity.response.FileResponse;
import plus.xyc.server.i18n.file.entity.response.FileUploadResponse;

import java.util.List;

import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 分支 服务类
 * </p>
 *
 * @author generator
 * @since 2025-06-17
 */
public interface FileService extends IService<File> {

    PageResult<FileResponse> list(PageRequest page, FileListRequest request);
    File findById(Long id);
    FileUploadResponse upload(FileUploadRequest request);
    void delete(Long fileId);
    List<List<String>> preview(Long fileId);
    void importFile(FileImportRequest request);
    void rename(FileRenameRequest request);
    void updateFile(FileUploadRequest request);
    File findByName(Long moduleId, String name);
    void updateBatch(FileUploadRequest request);
    List<FileResponse> all(Long moduleId);

}
