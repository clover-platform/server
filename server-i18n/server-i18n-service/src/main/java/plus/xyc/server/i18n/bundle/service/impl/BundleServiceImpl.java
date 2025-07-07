package plus.xyc.server.i18n.bundle.service.impl;

import com.github.pagehelper.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.bundle.entity.dto.Bundle;
import plus.xyc.server.i18n.bundle.entity.mapstruct.BundleMapStruct;
import plus.xyc.server.i18n.bundle.entity.request.BundleCreateRequest;
import plus.xyc.server.i18n.bundle.entity.request.BundleQueryRequest;
import plus.xyc.server.i18n.bundle.mapper.BundleMapper;
import plus.xyc.server.i18n.bundle.service.BundleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件包 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
@Slf4j
public class BundleServiceImpl extends ServiceImpl<BundleMapper, Bundle> implements BundleService {

    @Resource
    private BundleMapStruct mapStruct;

    @Override
    public PageResult<Bundle> query(PageRequest pr, BundleQueryRequest request) {
        try(Page<Bundle> page = pr.start()) {
            baseMapper.findByModuleId(request.getModuleId());
            return PageResult.of(page);
        }
    }

    @Override
    @DistributedLock(value = "#request.moduleId + ':bundle:create'")
    public void create(BundleCreateRequest request) {
        log.info("create bundle {}", request);
    }
}
