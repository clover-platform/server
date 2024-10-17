package plus.xyc.server.wiki.page.service;

import plus.xyc.server.wiki.page.entity.dto.PageLastVersion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generator
 * @since 2024-07-28
 */
public interface PageLastVersionService extends IService<PageLastVersion> {

    Long getLastVersion(Long pageId);
    Long getVersion(Long pageId);

}
