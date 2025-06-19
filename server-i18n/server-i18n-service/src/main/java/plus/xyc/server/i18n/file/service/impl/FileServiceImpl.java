package plus.xyc.server.i18n.file.service.impl;

import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.file.entity.dto.File;
import plus.xyc.server.i18n.file.entity.request.FileListRequest;
import plus.xyc.server.i18n.file.entity.request.FileUploadRequest;
import plus.xyc.server.i18n.file.mapper.FileMapper;
import plus.xyc.server.i18n.file.service.FileRevisionService;
import plus.xyc.server.i18n.file.service.FileService;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cache.annotation.CacheEvict;
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
    @Resource
    private FileRevisionService fileRevisionService;

    @Override
    public PageResult<File> list(PageRequest pr, FileListRequest request) {
        try (Page<File> page = pr.start()) {
            baseMapper.list(request);
            return PageResult.of(page);
        }
    }

    @Override
    @Cacheable(value = "i18n:file#1h", key = "#id")
    public File findById(Long id) {
        return baseMapper.selectById(id);
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

            JSONObject importConfig = new JSONObject();
            importConfig.put("type", "json");
            // 插入文件
            Date now = new Date();
            File fileEntity = new File();
            fileEntity.setModuleId(moduleId);
            fileEntity.setName(file.getName());
            fileEntity.setUploadUserId(userId);
            fileEntity.setUpdateUserId(userId);
            fileEntity.setUploadTime(now);
            fileEntity.setUpdateTime(now);
            fileEntity.setImportConfig(importConfig);
            fileEntity.setImportStatus(1);
            save(fileEntity);

            // 导入词条
            List<Entry> entries = new ArrayList<>();
            json.forEach((key, value) -> {
                Entry entry = new Entry();
                entry.setModuleId(moduleId);
                entry.setIdentifier(key);
                entry.setValue(value.toString());
                entry.setCreateTime(now);
                entry.setUpdateTime(now);
                entries.add(entry);
            });
            fileRevisionService.init(fileEntity.getId(), userId, urlStr, entries);
        } catch (Exception e) {
            log.error("读取远程json异常，url: {}", urlStr, e);
        }
    }

    private void initExcel(Long moduleId, Long userId, FileUploadRequest.FileItem file) {
        log.info("import excel file: {}", file);
        Date now = new Date();
        File fileEntity = new File();
        fileEntity.setModuleId(moduleId);
        fileEntity.setName(file.getName());
        fileEntity.setUploadUserId(userId);
        fileEntity.setUpdateUserId(userId);
        fileEntity.setUploadTime(now);
        fileEntity.setUpdateTime(now);
        JSONObject importConfig = new JSONObject();
        importConfig.put("type", "excel");
        importConfig.put("url", file.getUrl());
        fileEntity.setImportConfig(importConfig);
        fileEntity.setImportStatus(0);
        save(fileEntity);
    }

    @Override
    @Transactional
    public void upload(FileUploadRequest request) {
        log.info("upload file: {}", request);
        request.getFiles().forEach(file -> {
            if (file.getName().endsWith(".json")) {
                // 直接导入
                importJson(request.getModuleId(), request.getUserId(), file);
            } else if (file.getName().endsWith(".xlsx") || file.getName().endsWith(".xls")) {
                // 无法直接导入，需要用户配置列对应关系
                initExcel(request.getModuleId(), request.getUserId(), file);
            }
        });
    }

    @Override
    @CacheEvict(value = "i18n:file", key = "#fileId")
    public void delete(Long fileId) {
        UpdateWrapper<File> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(File::getDeleted, true).eq(File::getId, fileId);
        update(wrapper);
    }

}
