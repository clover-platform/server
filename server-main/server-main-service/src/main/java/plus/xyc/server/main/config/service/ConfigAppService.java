package plus.xyc.server.main.config.service;

import plus.xyc.server.main.config.entity.dto.ConfigApp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 应用 服务类
 * </p>
 *
 * @author generator
 * @since 2025-02-20
 */
public interface ConfigAppService extends IService<ConfigApp> {

    List<ConfigApp> all();

}
