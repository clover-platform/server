package plus.xyc.server.i18n.module.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.i18n.module.entity.dto.ModuleTargetLanguage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 目标语言 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface ModuleTargetLanguageMapper extends BaseMapper<ModuleTargetLanguage> {

    List<ModuleTargetLanguage> findByModuleId(@Param("moduleId") Long moduleId);
    ModuleTargetLanguage findOneByModuleIdAndCode(@Param("moduleId") Long moduleId, @Param("code") String code);

}
