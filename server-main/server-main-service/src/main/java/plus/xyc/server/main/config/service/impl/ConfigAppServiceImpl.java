package plus.xyc.server.main.config.service.impl;

import org.springframework.cache.annotation.Cacheable;
import plus.xyc.server.main.config.entity.dto.ConfigApp;
import plus.xyc.server.main.config.mapper.ConfigAppMapper;
import plus.xyc.server.main.config.service.ConfigAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 应用 服务实现类
 * </p>
 *
 * @author generator
 * @since 2025-02-20
 */
@Service
public class ConfigAppServiceImpl extends ServiceImpl<ConfigAppMapper, ConfigApp> implements ConfigAppService {

    @Override
    @Cacheable(value = "config:apps")
    public List<ConfigApp> all() {
        return baseMapper.findByEnable(true);
    }
}
