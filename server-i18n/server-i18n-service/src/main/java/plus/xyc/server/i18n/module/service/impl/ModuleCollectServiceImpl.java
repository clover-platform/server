package plus.xyc.server.i18n.module.service.impl;

import plus.xyc.server.i18n.module.entity.dto.Module;
import plus.xyc.server.i18n.module.entity.dto.ModuleCollect;
import plus.xyc.server.i18n.module.mapper.ModuleCollectMapper;
import plus.xyc.server.i18n.module.service.ModuleCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;

import plus.xyc.server.i18n.common.enums.I18nCode;

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

    @Override
    @CacheEvict(value = "module:collect", key = "#userId + ':' + #projectId")
    public void add(Long userId, Long projectId, Long moduleId) {
        lambdaQuery().eq(ModuleCollect::getModuleId, moduleId).eq(ModuleCollect::getUserId, userId).oneOpt().ifPresent(collect -> {
            throw ResultException.of(I18nCode.MODULE_COLLECT_EXIST.code, MessageUtils.get(I18nCode.MODULE_COLLECT_EXIST.key));
        });
        ModuleCollect collect = new ModuleCollect();
        collect.setModuleId(moduleId);
        collect.setUserId(userId);
        save(collect);
    }

    @Override
    @Cacheable(value = "module:collect", key = "#userId + ':' + #projectId")
    public List<Module> my(Long userId, Long projectId) {
        return baseMapper.my(userId, projectId);
    }

    @Override
    @CacheEvict(value = "module:collect", key = "#userId + ':' + #projectId")
    public void cancel(Long userId, Long projectId, Long moduleId) {
        lambdaUpdate().eq(ModuleCollect::getModuleId, moduleId).eq(ModuleCollect::getUserId, userId).remove();
    }

    @Override
    @CacheEvict(value = "module:collect", allEntries = true)
    public void cancel(Long moduleId) {
        lambdaUpdate().eq(ModuleCollect::getModuleId, moduleId).remove();
    }

}
