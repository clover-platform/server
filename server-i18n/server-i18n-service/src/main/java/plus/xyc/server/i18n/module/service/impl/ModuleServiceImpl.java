package plus.xyc.server.i18n.module.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.module.entity.dto.Module;
import plus.xyc.server.i18n.module.entity.mapstruct.ModuleMapStruct;
import plus.xyc.server.i18n.module.entity.request.ModuleQueryRequest;
import plus.xyc.server.i18n.module.entity.response.ModuleResponse;
import plus.xyc.server.i18n.module.entity.response.SizeResponse;
import plus.xyc.server.i18n.module.mapper.ModuleMapper;
import plus.xyc.server.i18n.module.service.ModuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 项目 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
@Slf4j
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, Module> implements ModuleService {

    @Resource
    private ModuleMapStruct moduleMapStruct;

    @Override
    public PageResult<ModuleResponse> query(PageQueryRequest pageRequest, ModuleQueryRequest query) {
        Page<Module> page = pageRequest.toPage();
        List<Module> modules = baseMapper.query(page, pageRequest.getKeyword(), query);
        return PageResult.of(page.getTotal(), wrapResponse(modules));
    }

    public List<ModuleResponse> wrapResponse(List<Module> modules) {
        List<Long> moduleIds = modules.stream().map(Module::getId).toList();
        List<SizeResponse> memberSizes = memberSizes(moduleIds);
        List<SizeResponse> targetSizes = targetSizes(moduleIds);
        return modules.stream().map(module -> {
            ModuleResponse response = moduleMapStruct.toModuleResponse(module);
            response.setMemberSize(memberSizes.stream().filter(size -> size.getId().equals(module.getId())).findFirst().map(SizeResponse::getSize).orElse(0));
            response.setTargetSize(targetSizes.stream().filter(size -> size.getId().equals(module.getId())).findFirst().map(SizeResponse::getSize).orElse(0));
            return response;
        }).toList();
    }

    public List<SizeResponse> memberSizes(List<Long> moduleIds) {
        if(moduleIds.isEmpty())
            return List.of();
        return baseMapper.memberSizes(moduleIds);
    }

    public List<SizeResponse> targetSizes(List<Long> moduleIds) {
        if(moduleIds.isEmpty())
            return List.of();
        return baseMapper.targetSizes(moduleIds);
    }
}
