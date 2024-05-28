package plus.xyc.server.i18n.module.service;

import plus.xyc.server.i18n.module.entity.dto.ModuleCount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 模块统计 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-24
 */
public interface ModuleCountService extends IService<ModuleCount> {

    void updateCount(Long id);
    void updateCount(Long id, Long branchId);
    void updateCount(Long id, Long branchId, String language);

}
