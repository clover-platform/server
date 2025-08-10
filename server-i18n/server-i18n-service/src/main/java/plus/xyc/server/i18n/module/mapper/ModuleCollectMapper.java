package plus.xyc.server.i18n.module.mapper;

import plus.xyc.server.i18n.module.entity.dto.Module;
import plus.xyc.server.i18n.module.entity.dto.ModuleCollect;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2025-06-30
 */
public interface ModuleCollectMapper extends BaseMapper<ModuleCollect> {

    List<Module> my(Long userId, Long teamId);

}
