package plus.xyc.server.i18n.module.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.i18n.language.entity.mapstruct.LanguageMapStruct;
import plus.xyc.server.i18n.language.entity.response.LanguageResponse;
import plus.xyc.server.i18n.language.service.LanguageService;
import plus.xyc.server.i18n.module.entity.dto.ModuleCount;
import plus.xyc.server.i18n.module.entity.dto.ModuleTargetLanguage;
import plus.xyc.server.i18n.module.entity.response.ModuleLanguageResponse;
import plus.xyc.server.i18n.module.mapper.ModuleCountMapper;
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
@Slf4j
public class ModuleTargetLanguageServiceImpl extends ServiceImpl<ModuleTargetLanguageMapper, ModuleTargetLanguage> implements ModuleTargetLanguageService {

    @Resource
    private LanguageService languageService;
    @Resource
    private LanguageMapStruct languageMapStruct;
    @Resource
    private ModuleCountMapper moduleCountMapper;

    @Override
    public List<ModuleLanguageResponse> languages(Long id) {
        List<LanguageResponse> all = languageService.all();
        List<ModuleTargetLanguage> targets = baseMapper.findByModuleId(id);
        List<ModuleCount> counts = moduleCountMapper.findByModuleId(id);
        return targets.stream()
                .map(target -> {
                    LanguageResponse language = all.stream()
                            .filter(l -> l.getCode().equals(target.getCode()))
                            .findFirst()
                            .orElse(null);
                    ModuleLanguageResponse response = languageMapStruct.toModuleLanguageResponse(language);
                    long totalEntry = counts.stream()
                            .filter(count -> count.getCode().equals(target.getCode()))
                            .mapToLong(ModuleCount::getTotalEntry)
                            .sum();
                    long translatedEntry = counts.stream()
                            .filter(count -> count.getCode().equals(target.getCode()))
                            .mapToLong(ModuleCount::getTranslatedEntry)
                            .sum();
                    long verifiedEntry = counts.stream()
                            .filter(count -> count.getCode().equals(target.getCode()))
                            .mapToLong(ModuleCount::getVerifiedEntry)
                            .sum();
                    long totalEntryLength = counts.stream()
                            .filter(count -> count.getCode().equals(target.getCode()))
                            .mapToLong(ModuleCount::getTotalEntryLength)
                            .sum();
                    response.setTotalEntry(totalEntry);
                    response.setTranslatedEntry(translatedEntry);
                    response.setVerifiedEntry(verifiedEntry);
                    response.setTotalEntryLength(totalEntryLength);
                    return response;
                })
                .toList();
    }

}
