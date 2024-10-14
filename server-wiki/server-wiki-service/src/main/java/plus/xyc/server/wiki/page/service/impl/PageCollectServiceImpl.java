package plus.xyc.server.wiki.page.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.wiki.page.entity.dto.PageCollect;
import plus.xyc.server.wiki.page.entity.request.CollectPageRequest;
import plus.xyc.server.wiki.page.mapper.PageCollectMapper;
import plus.xyc.server.wiki.page.service.PageCacheService;
import plus.xyc.server.wiki.page.service.PageCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 收藏夹 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-10-14
 */
@Service
@Slf4j
public class PageCollectServiceImpl extends ServiceImpl<PageCollectMapper, PageCollect> implements PageCollectService {

    @Resource
    private PageCacheService pageCacheService;

    @Override
    public void collect(CollectPageRequest request) {
        log.info("{}",request);
        pageCacheService.clearCache(request.getId());
        if(request.getCollect()) {
            int size = getBaseMapper().countByPageIdAndUserId(request.getId(), request.getUserId());
            if(size > 0)
                return;
            PageCollect pageCollect = new PageCollect();
            pageCollect.setPageId(request.getId());
            pageCollect.setUserId(request.getUserId());
            pageCollect.setBookId(request.getBookId());
            pageCollect.setCreateTime(new Date());
            save(pageCollect);
        }else{
            getBaseMapper().deleteByPageIdAndUserId(request.getId(), request.getUserId());
        }
    }
}
