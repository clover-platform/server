package plus.xyc.server.i18n.module.service.impl;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.mapper.EntryMapper;
import plus.xyc.server.i18n.language.entity.mapstruct.LanguageMapStruct;
import plus.xyc.server.i18n.language.entity.response.LanguageResponse;
import plus.xyc.server.i18n.language.service.LanguageService;
import plus.xyc.server.i18n.module.entity.dto.ModuleTargetLanguage;
import plus.xyc.server.i18n.module.entity.response.ModuleLanguageResponse;
import plus.xyc.server.i18n.module.mapper.ModuleTargetLanguageMapper;
import plus.xyc.server.i18n.module.service.ModuleTargetLanguageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 目标语言 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
public class ModuleTargetLanguageServiceImpl extends ServiceImpl<ModuleTargetLanguageMapper, ModuleTargetLanguage> implements ModuleTargetLanguageService {

    @Resource
    private LanguageService languageService;
    @Resource
    private LanguageMapStruct languageMapStruct;
    @Resource
    private EntryMapper entryMapper;

    @Override
    public List<ModuleLanguageResponse> languages(Long id) {
        List<LanguageResponse> all = languageService.all();
        List<ModuleTargetLanguage> targets = baseMapper.findByModuleId(id);
        return targets.stream()
                .map(target -> {
                    LanguageResponse language = all.stream()
                            .filter(l -> l.getCode().equals(target.getCode()))
                            .findFirst()
                            .orElse(null);
                    return languageMapStruct.toModuleLanguageResponse(language);
                })
                .toList();
    }

    @Override
    @Transactional
    @DistributedLock(value = "module:update:count", key = "#id")
    public void updateCount(Long id) {
        List<ModuleTargetLanguage> targets = baseMapper.findByModuleId(id);
        targets.forEach(target -> {
            this.updateCount(id, target.getCode());
        });
    }

    @Override
    @Transactional
    @DistributedLock(value = "module:update:count", key = "#id + ':' + #language")
    public void updateCount(Long id, String language) {
        ModuleTargetLanguage target = baseMapper.findOneByModuleIdAndCode(id, language);
        List<Entry> entries = entryMapper.findByModuleId(id);
    }
}
