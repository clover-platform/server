package plus.xyc.server.i18n.language.mapper;

import org.apache.ibatis.annotations.Param;
import plus.xyc.server.i18n.language.entity.dto.Language;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.i18n.language.entity.response.LanguageResponse;

import java.util.List;

/**
 * <p>
 * 语言 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface LanguageMapper extends BaseMapper<Language> {

    List<LanguageResponse> selectByLang(@Param("lang") String lang);

}
