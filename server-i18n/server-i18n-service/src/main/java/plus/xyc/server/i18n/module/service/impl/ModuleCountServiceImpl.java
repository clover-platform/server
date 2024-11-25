package plus.xyc.server.i18n.module.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.branch.entity.dto.Branch;
import plus.xyc.server.i18n.branch.mapper.BranchMapper;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.mapper.EntryMapper;
import plus.xyc.server.i18n.entry.mapper.EntryStateMapper;
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
    private BranchMapper branchMapper;
    @Resource
    private ModuleTargetLanguageMapper moduleTargetLanguageMapper;

    @Override
    @Transactional
    @DistributedLock(value = "'module:update:count:'+#id")
    public void updateCount(Long id) {
        List<ModuleTargetLanguage> targets = moduleTargetLanguageMapper.findByModuleId(id);
        List<Branch> branches = branchMapper.findByModuleId(id);
        targets.forEach(target -> {
            branches.forEach(branch -> {
                this.updateCount(id, branch.getId(), target.getCode());
            });
        });
    }

    @Override
    @Transactional
    @DistributedLock(value = "'module:update:count:'+#id+':'+#branchId")
    public void updateCount(Long id, Long branchId) {
        List<ModuleTargetLanguage> targets = moduleTargetLanguageMapper.findByModuleId(id);
        targets.forEach(target -> {
            this.updateCount(id, branchId, target.getCode());
        });
    }

    @Override
    @Transactional
    @DistributedLock(value = "'module:update:count:' + #id + ':' + #branchId + ':' + #language")
    public void updateCount(Long id, Long branchId, String language) {
        List<Entry> entries = entryMapper.findByModuleIdAndBranchId(id, branchId);
        Branch branch = branchMapper.findOneById(branchId);
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
        ModuleCount count = baseMapper.findOneByModuleIdAndBranchIdAndCode(id, branchId, language);
        if(count == null) {
            count = new ModuleCount();
            count.setModuleId(id);
            count.setBranchId(branchId);
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
        List<ModuleCount> counts = baseMapper.findByModuleIdIn(moduleIds);
        return moduleIds.stream().map(id -> {
            ModuleCountResponse response = new ModuleCountResponse();
            response.setModuleId(id);
            response.setWordCount(counts.stream().filter(count -> count.getModuleId().equals(id)).mapToLong(ModuleCount::getTotalEntry).sum());
            return response;
        }).toList();
    }
}
