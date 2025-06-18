package plus.xyc.server.i18n.file.service;

import plus.xyc.server.i18n.file.entity.dto.File;
import plus.xyc.server.i18n.file.entity.request.FileListRequest;
import plus.xyc.server.i18n.file.entity.request.FileUploadRequest;

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

    PageResult<File> list(PageRequest page, FileListRequest request);
    File findByName(Long moduleId, String name);
    void upload(FileUploadRequest request);

}
