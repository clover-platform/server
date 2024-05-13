package plus.xyc.server.i18n.language.service;

import plus.xyc.server.i18n.language.entity.dto.Language;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.language.entity.response.LanguageResponse;

import java.util.List;

/**
 * <p>
 * 语言 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface LanguageService extends IService<Language> {

    List<LanguageResponse> all();

}
