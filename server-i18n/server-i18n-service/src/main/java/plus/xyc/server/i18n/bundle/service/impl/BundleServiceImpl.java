package plus.xyc.server.i18n.bundle.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.bundle.entity.dto.Bundle;
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


    @Override
    public PageResult<Bundle> query(PageRequest page, BundleQueryRequest request) {
        log.info("query bundle {}", page);
        log.info("query bundle {}", request);
        return null;
    }
}
