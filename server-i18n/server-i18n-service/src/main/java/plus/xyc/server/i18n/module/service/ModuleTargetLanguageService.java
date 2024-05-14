package plus.xyc.server.i18n.module.service;

import plus.xyc.server.i18n.language.entity.response.LanguageResponse;
import plus.xyc.server.i18n.module.entity.dto.ModuleTargetLanguage;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.module.entity.response.ModuleLanguageResponse;

import java.util.List;

/**
 * <p>
 * 目标语言 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface ModuleTargetLanguageService extends IService<ModuleTargetLanguage> {

    List<ModuleLanguageResponse> languages(Long id);

}
