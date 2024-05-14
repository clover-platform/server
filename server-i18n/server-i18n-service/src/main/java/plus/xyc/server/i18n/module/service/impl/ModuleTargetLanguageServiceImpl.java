package plus.xyc.server.i18n.module.service.impl;

import jakarta.annotation.Resource;
import plus.xyc.server.i18n.language.entity.response.LanguageResponse;
import plus.xyc.server.i18n.language.service.LanguageService;
import plus.xyc.server.i18n.module.entity.dto.ModuleTargetLanguage;
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

    @Override
    public List<LanguageResponse> languages(Long id) {
        List<LanguageResponse> all = languageService.all();
        List<ModuleTargetLanguage> targets = baseMapper.findByModuleId(id);
        return targets.stream()
                .map(target -> {
                    LanguageResponse language = all.stream()
                            .filter(l -> l.getCode().equals(target.getCode()))
                            .findFirst()
                            .orElse(null);
                    if (language != null) {
                        language.setId(target.getId());
                    }
                    return language;
                })
                .toList();
    }
}
