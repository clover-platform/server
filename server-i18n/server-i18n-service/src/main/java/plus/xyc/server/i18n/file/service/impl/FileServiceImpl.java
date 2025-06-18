package plus.xyc.server.i18n.file.service.impl;

import plus.xyc.server.i18n.file.entity.dto.File;
import plus.xyc.server.i18n.file.entity.request.FileListRequest;
import plus.xyc.server.i18n.file.entity.request.FileUploadRequest;
import plus.xyc.server.i18n.file.mapper.FileMapper;
import plus.xyc.server.i18n.file.service.FileService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;

import jakarta.annotation.Resource;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import lombok.extern.slf4j.Slf4j;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.zkit.support.server.assets.api.service.AssetsOSSApiService;
import org.zkit.support.starter.boot.okhttp.HTTPService;
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
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Resource
    private HTTPService httpService;
    @DubboReference
    private AssetsOSSApiService assetsOSSApiService;

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

    // 导入 json 文件
    private void importJson(Long moduleId, Long userId, FileUploadRequest.FileItem file) {
        log.info("import json file: {}", file);
        String urlStr = file.getUrl();
        try {
            String signedUrl = assetsOSSApiService.sign(urlStr);
            log.info("signedUrl: {}", signedUrl);
            String jsonStr = httpService.get(signedUrl);
            JSONObject json = JSON.parseObject(jsonStr);
            log.info("远程json内容: {}", json);
        } catch (Exception e) {
            log.error("读取远程json异常，url: {}", urlStr, e);
        }
    }

    @Override
    public void upload(FileUploadRequest request) {
        log.info("upload file: {}", request);
        request.getFiles().forEach(file -> {
            // 不同的文件类型处理方式不同
            // json 文件
            if (file.getName().endsWith(".json")) {
                importJson(request.getModuleId(), request.getUserId(), file);
            }
        });
    }

}
