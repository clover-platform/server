package plus.xyc.server.i18n.module.service;

import plus.xyc.server.i18n.module.entity.dto.ModuleCount;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.module.entity.response.ModuleCountResponse;

import java.util.List;

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
    void updateCount(Long id, Long fileId);
    void updateCount(Long id, Long fileId, String language);
    List<ModuleCountResponse> getCounts(List<Long> moduleIds);
    List<ModuleCount> getCounts(Long moduleId, List<Long> fileIds);

}
