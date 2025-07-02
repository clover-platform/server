package plus.xyc.server.i18n.module.service.impl;

import plus.xyc.server.i18n.module.entity.dto.ModuleCollect;
import plus.xyc.server.i18n.module.mapper.ModuleCollectMapper;
import plus.xyc.server.i18n.module.service.ModuleCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2025-06-30
 */
@Service
public class ModuleCollectServiceImpl extends ServiceImpl<ModuleCollectMapper, ModuleCollect> implements ModuleCollectService {

    @Override
    public List<ModuleCollect> findByUserIdAndModuleIds(Long userId, List<Long> moduleIds) {
        if(moduleIds == null || moduleIds.isEmpty())
            return List.of();
        return lambdaQuery().in(ModuleCollect::getModuleId, moduleIds).eq(ModuleCollect::getUserId, userId).list();
    }

}
