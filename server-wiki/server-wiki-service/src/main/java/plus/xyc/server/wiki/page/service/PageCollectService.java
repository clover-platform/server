package plus.xyc.server.wiki.page.service;

import plus.xyc.server.wiki.page.entity.dto.PageCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.wiki.page.entity.request.CollectPageRequest;

/**
 * <p>
 * 收藏夹 服务类
 * </p>
 *
 * @author generator
 * @since 2024-10-14
 */
public interface PageCollectService extends IService<PageCollect> {

    void collect(CollectPageRequest request);

}
