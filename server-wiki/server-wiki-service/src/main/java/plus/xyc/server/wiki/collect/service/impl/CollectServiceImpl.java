package plus.xyc.server.wiki.collect.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.wiki.collect.entity.dto.Collect;
import plus.xyc.server.wiki.collect.entity.request.CollectRequest;
import plus.xyc.server.wiki.collect.mapper.CollectMapper;
import plus.xyc.server.wiki.collect.service.CollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.wiki.page.service.PageCacheService;

import java.util.Date;

/**
 * <p>
 * 收藏夹 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-11-01
 */
@Service
@Slf4j
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Resource
    private PageCacheService pageCacheService;

    @Override
    public void collect(CollectRequest request) {
        log.info("{}",request);
        pageCacheService.clearCache(request.getPageId());
        if(request.getCollect()) {
            int size = getBaseMapper().countByBookIdAndPageIdAndUserId(request.getBookId(), request.getPageId(), request.getUserId());
            if(size > 0)
                return;
            Collect pageCollect = new Collect();
            pageCollect.setPageId(request.getPageId());
            pageCollect.setUserId(request.getUserId());
            pageCollect.setBookId(request.getBookId());
            pageCollect.setCreateTime(new Date());
            save(pageCollect);
        }else{
            getBaseMapper().deleteByBookIdAndPageIdAndUserId(request.getBookId(), request.getPageId(), request.getUserId());
        }
    }

}
