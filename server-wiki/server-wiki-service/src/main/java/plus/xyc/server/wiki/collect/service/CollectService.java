package plus.xyc.server.wiki.collect.service;

import plus.xyc.server.wiki.collect.entity.dto.Collect;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.wiki.collect.entity.request.CollectRequest;

/**
 * <p>
 * 收藏夹 服务类
 * </p>
 *
 * @author generator
 * @since 2024-11-01
 */
public interface CollectService extends IService<Collect> {

    void collect(CollectRequest request);

}
