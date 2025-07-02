package plus.xyc.server.i18n.module.service;

import plus.xyc.server.i18n.module.entity.dto.ModuleCollect;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generator
 * @since 2025-06-30
 */
public interface ModuleCollectService extends IService<ModuleCollect> {

    List<ModuleCollect> findByUserIdAndModuleIds(Long userId, List<Long> moduleIds);

}
