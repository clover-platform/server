package plus.xyc.server.i18n.file.service.impl;

import plus.xyc.server.i18n.file.entity.dto.File;
import plus.xyc.server.i18n.file.entity.request.FileListRequest;
import plus.xyc.server.i18n.file.mapper.FileMapper;
import plus.xyc.server.i18n.file.service.FileService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;

/**
 * <p>
 * 分支 服务实现类
 * </p>
 *
 * @author generator
 * @since 2025-06-17
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Override
    public PageResult<File> list(PageRequest pr, FileListRequest request) {
        try (Page<File> page = pr.start()) {
            baseMapper.list(request);
            return PageResult.of(page);
        }
    }

    @Override
    @Cacheable(value = "i18n:file", key = "#moduleId + ':' + #name")
    public File findByName(Long moduleId, String name) {
        return baseMapper.findOneByModuleIdAndNameAndDeleted(moduleId, name, false);
    }

}
