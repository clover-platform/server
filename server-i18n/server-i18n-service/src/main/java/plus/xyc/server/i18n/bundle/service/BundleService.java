package plus.xyc.server.i18n.bundle.service;

import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.bundle.entity.dto.Bundle;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.bundle.entity.request.BundleQueryRequest;

/**
 * <p>
 * 文件包 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface BundleService extends IService<Bundle> {

    PageResult<Bundle> query(PageRequest page, BundleQueryRequest request);

}
