package plus.xyc.server.i18n.language.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.starter.boot.utils.MessageUtils;
import plus.xyc.server.i18n.configuration.AppConfiguration;
import plus.xyc.server.i18n.language.entity.dto.Language;
import plus.xyc.server.i18n.language.entity.response.LanguageResponse;
import plus.xyc.server.i18n.language.mapper.LanguageMapper;
import plus.xyc.server.i18n.language.service.LanguageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 语言 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
@Slf4j
public class LanguageServiceImpl extends ServiceImpl<LanguageMapper, Language> implements LanguageService {

    @Resource
    private AppConfiguration configuration;

    @Override
    @Cacheable(value = "language#1d", key = "#root.methodName")
    public List<LanguageResponse> all() {
        String lang = MessageUtils.getLocale();
        List<LanguageResponse> fallbackLangList = selectByLang(configuration.getDataFallbackLanguage());
        List<LanguageResponse> langList = selectByLang(lang);
        return fallbackLangList.stream().map(item ->
                langList.stream().filter(langItem ->
                        langItem.getCode().equals(item.getCode())).findFirst().orElse(item)
        ).toList();
    }

    public List<LanguageResponse> selectByLang(String lang) {
        return baseMapper.selectByLang(lang);
    }

    @Override
    public LanguageResponse getByCode(String code) {
        LanguageResponse response = baseMapper.selectOneByCodeAndLanguage(code, MessageUtils.getLocale());
        if(response == null)
            response = baseMapper.selectOneByCodeAndLanguage(code, configuration.getDataFallbackLanguage());
        return response;
    }
}
