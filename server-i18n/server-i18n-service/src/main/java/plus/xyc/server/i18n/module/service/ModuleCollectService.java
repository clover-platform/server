package plus.xyc.server.i18n.module.service;

import plus.xyc.server.i18n.module.entity.dto.ModuleCollect;
import plus.xyc.server.i18n.module.entity.dto.Module;

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
    void add(Long userId, Long teamId, Long moduleId);
    List<Module> my(Long userId, Long teamId);
    void cancel(Long userId, Long teamId, Long moduleId);
    void cancel(Long moduleId);

}
