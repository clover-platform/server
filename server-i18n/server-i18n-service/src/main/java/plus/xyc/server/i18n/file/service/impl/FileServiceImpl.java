package plus.xyc.server.i18n.file.service.impl;

import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.request.EntryImportRequest;
import plus.xyc.server.i18n.entry.service.EntryService;
import plus.xyc.server.i18n.file.entity.dto.File;
import plus.xyc.server.i18n.file.entity.dto.FileRevision;
import plus.xyc.server.i18n.file.entity.mapstruct.FileMapStruct;
import plus.xyc.server.i18n.file.entity.request.FileImportRequest;
import plus.xyc.server.i18n.file.entity.request.FileListRequest;
import plus.xyc.server.i18n.file.entity.request.FileRenameRequest;
import plus.xyc.server.i18n.file.entity.request.FileUploadRequest;
import plus.xyc.server.i18n.file.entity.response.FileResponse;
import plus.xyc.server.i18n.file.mapper.FileMapper;
import plus.xyc.server.i18n.file.service.FileRevisionService;
import plus.xyc.server.i18n.file.service.FileService;
import plus.xyc.server.i18n.module.entity.dto.ModuleCount;
import plus.xyc.server.i18n.module.service.ModuleCountService;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;

import cn.idev.excel.FastExcel;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.listener.ReadListener;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Resource
    private FileMapStruct fileMapStruct;
    @Resource
    private EntryService entryService;
    @Resource
    private ModuleCountService moduleCountService;

    @Override
    public PageResult<FileResponse> list(PageRequest pr, FileListRequest request) {
        try (Page<File> page = pr.start()) {
            baseMapper.list(request);
            List<FileResponse> fileResponses = page.getResult().stream().map(fileMapStruct::toFileResponse)
                    .collect(Collectors.toList());
            List<Long> fileIds = fileResponses.stream().map(FileResponse::getId).collect(Collectors.toList());
            List<FileRevision> fileRevisions = fileRevisionService.findListByFileIds(fileIds);
            List<ModuleCount> moduleCounts = moduleCountService.getCounts(request.getModuleId(), fileIds);
            log.info("moduleCounts: {}", JSON.toJSONString(moduleCounts));
            fileResponses.forEach(fileResponse -> {
                fileResponse.setRevisionVersion(fileRevisions.stream()
                        .filter(fileRevision -> fileRevision.getFileId().equals(fileResponse.getId())).findFirst()
                        .map(FileRevision::getVersion).orElse(0));
                ModuleCount count = moduleCounts.stream()
                        .filter(moduleCount -> moduleCount.getFileId().equals(fileResponse.getId())).findFirst()
                        .orElse(null);
                fileResponse.setWordCount(count == null ? 0 : count.getTotalEntry().intValue());
            });
            return PageResult.of(page.getTotal(), fileResponses);
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

    @Override
    public List<List<String>> preview(Long fileId) {
        File file = getById(fileId);
        JSONObject importConfig = JSON.parseObject(JSON.toJSONString(file.getImportConfig()));
        String url = importConfig.getString("url");
        String signedUrl = assetsOSSApiService.sign(url);

        InputStream inputStream = httpService.download(signedUrl);

        List<List<String>> all = new ArrayList<>();
        FastExcel.read(inputStream, new ReadListener<Map<Integer, String>>() {
            @Override
            public void invoke(Map<Integer, String> data, AnalysisContext context) {
                List<String> row = new ArrayList<>();
                data.forEach((key, value) -> {
                    row.add(value);
                });
                all.add(row);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
            }
        }).sheet(0).headRowNumber(0).numRows(5).doRead();
        return all;
    }

    @Override
    @Transactional
    public void importFile(FileImportRequest request) {
        log.info("import file: {}", request);
        File file = getById(request.getFileId());
        Boolean skipFirstRow = request.getSkipFirstRow();
        JSONObject importConfig = JSON.parseObject(JSON.toJSONString(file.getImportConfig()));
        String url = importConfig.getString("url");
        String signedUrl = assetsOSSApiService.sign(url);

        InputStream inputStream = httpService.download(signedUrl);

        EntryImportRequest entryImportRequest = new EntryImportRequest();
        entryImportRequest.setFileId(file.getId());
        entryImportRequest.setModuleId(file.getModuleId());
        entryImportRequest.setUserId(request.getUserId());
        entryImportRequest.setFileUrl(url);
        List<EntryImportRequest.Entry> entries = new ArrayList<>();

        Map<String, String> config = request.getConfig();
        log.info("config: {}", JSON.toJSONString(config));
        FastExcel.read(inputStream, new ReadListener<Map<Integer, String>>() {
            @Override
            public void invoke(Map<Integer, String> data, AnalysisContext context) {
                log.info("read row: {}", JSON.toJSONString(data));
                EntryImportRequest.Entry entry = new EntryImportRequest.Entry();
                List<EntryImportRequest.Result> results = new ArrayList<>();

                config.forEach((key, value) -> {
                    if (value.startsWith("target:")) { // 翻译结果列
                        String content = data.get(Integer.parseInt(key));
                        if (content == null || content.isEmpty()) {
                            return;
                        }
                        String language = value.replace("target:", "");
                        EntryImportRequest.Result result = new EntryImportRequest.Result();
                        result.setLanguage(language);
                        result.setContent(content);
                        results.add(result);
                    } else { // invoke method 比如 identifier 则调用 setIdentifier
                        String method = value;
                        method = "set" + method.substring(0, 1).toUpperCase() + method.substring(1);
                        try {
                            entry.getClass().getMethod(method, String.class).invoke(entry,
                                    data.get(Integer.parseInt(key)));
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException
                                | SecurityException e) {
                            log.error("invoke method error: {}", e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                });
                entry.setResults(results);
                entries.add(entry);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
            }
        }).sheet(0).headRowNumber(skipFirstRow ? 1 : 0).doRead();
        entryImportRequest.setEntries(entries);
        entryService.importEntries(entryImportRequest);

        // 更新文件导入状态
        importConfig.put("config", config);
        file.setImportStatus(1);
        file.setImportConfig(importConfig);
        file.setUpdateTime(new Date());
        updateById(file);
    }

    @Override
    @Transactional
    public void rename(FileRenameRequest request) {
        UpdateWrapper<File> wrapper = new UpdateWrapper<>();
        wrapper.lambda()
            .set(File::getUpdateUserId, request.getUserId())
            .set(File::getUpdateTime, new Date())
            .set(File::getName, request.getName())
            .eq(File::getId, request.getFileId());
        update(wrapper);
    }

}
