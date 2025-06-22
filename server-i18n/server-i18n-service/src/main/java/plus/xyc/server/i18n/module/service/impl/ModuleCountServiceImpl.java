package plus.xyc.server.i18n.module.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.mapper.EntryMapper;
import plus.xyc.server.i18n.entry.mapper.EntryStateMapper;
import plus.xyc.server.i18n.file.entity.dto.File;
import plus.xyc.server.i18n.file.mapper.FileMapper;
import plus.xyc.server.i18n.module.entity.dto.ModuleCount;
import plus.xyc.server.i18n.module.entity.dto.ModuleTargetLanguage;
import plus.xyc.server.i18n.module.entity.response.ModuleCountResponse;
import plus.xyc.server.i18n.module.mapper.ModuleCountMapper;
import plus.xyc.server.i18n.module.mapper.ModuleTargetLanguageMapper;
import plus.xyc.server.i18n.module.service.ModuleCountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 模块统计 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-24
 */
@Service
@Slf4j
public class ModuleCountServiceImpl extends ServiceImpl<ModuleCountMapper, ModuleCount> implements ModuleCountService {

    @Resource
    private EntryMapper entryMapper;
    @Resource
    private EntryStateMapper entryStateMapper;
    @Resource
    private FileMapper fileMapper;
    @Resource
    private ModuleTargetLanguageMapper moduleTargetLanguageMapper;

    @Override
    @Transactional
    @DistributedLock(value = "'module:update:count:'+#id")
    public void updateCount(Long id) {
        // TODO updateCount
        // List<ModuleTargetLanguage> targets = moduleTargetLanguageMapper.findByModuleId(id);
        // List<Branch> branches = branchMapper.findByModuleId(id);
        // targets.forEach(target -> {
        //     branches.forEach(branch -> {
        //         this.updateCount(id, branch.getId(), target.getCode());
        //     });
        // });
    }

    @Override
    @Transactional
    @DistributedLock(value = "'module:update:count:'+#id+':'+#fileId")
    public void updateCount(Long id, Long fileId) {
        List<ModuleTargetLanguage> targets = moduleTargetLanguageMapper.findByModuleId(id);
        targets.forEach(target -> {
            this.updateCount(id, fileId, target.getCode());
        });
    }

    @Override
    @Transactional
    @DistributedLock(value = "'module:update:count:' + #id + ':' + #fileId + ':' + #language")
    public void updateCount(Long id, Long fileId, String language) {
        List<Entry> entries = entryMapper.findByModuleIdAndFileId(id, fileId);
        File branch = fileMapper.selectById(fileId);
        log.info("entries: {}", entries);
        EntryCountRequest request = new EntryCountRequest();
        request.setModuleId(id);
        request.setLanguage(language);
        request.setBranch(branch.getName());
        Long translated = entryStateMapper.countTranslated(request);
        Long verified = entryStateMapper.countVerified(request);
        Long total = entryMapper.countTotal(request);
        long totalLength = 0L;
        for (Entry entry : entries) {
            totalLength += entry.getValue().length();
        }
        ModuleCount count = baseMapper.findOneByModuleIdAndFileIdAndCode(id, fileId, language);
        if(count == null) {
            count = new ModuleCount();
            count.setModuleId(id);
            count.setFileId(fileId);
            count.setCode(language);
            baseMapper.insert(count);
        }
        count.setTranslatedEntry(translated);
        count.setVerifiedEntry(verified);
        count.setTotalEntry(total);
        count.setTotalEntryLength(totalLength);
        UpdateWrapper<ModuleCount> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", count.getId());
        baseMapper.update(count, wrapper);
    }

    @Override
    public List<ModuleCountResponse> getCounts(List<Long> moduleIds) {
        if(moduleIds == null || moduleIds.isEmpty())
            return List.of();
        List<ModuleCount> counts = baseMapper.findByModuleIdIn(moduleIds);
        return moduleIds.stream().map(id -> {
            ModuleCountResponse response = new ModuleCountResponse();
            response.setModuleId(id);
            response.setWordCount(counts.stream().filter(count -> count.getModuleId().equals(id)).mapToLong(ModuleCount::getTotalEntry).sum());
            return response;
        }).toList();
    }

    @Override
    public List<ModuleCount> getCounts(Long moduleId, List<Long> fileIds) {
        if(fileIds == null || fileIds.isEmpty())
            return List.of();
        return baseMapper.findByModuleIdAndFileIdIn(moduleId, fileIds);
    }
}
