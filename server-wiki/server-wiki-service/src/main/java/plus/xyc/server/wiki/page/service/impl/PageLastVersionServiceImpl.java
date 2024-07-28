package plus.xyc.server.wiki.page.service.impl;

import plus.xyc.server.wiki.page.entity.dto.PageLastVersion;
import plus.xyc.server.wiki.page.mapper.PageLastVersionMapper;
import plus.xyc.server.wiki.page.service.PageLastVersionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-07-28
 */
@Service
public class PageLastVersionServiceImpl extends ServiceImpl<PageLastVersionMapper, PageLastVersion> implements PageLastVersionService {

    @Override
    public Long getVersion(Long pageId) {
        PageLastVersion lastVersion = getBaseMapper().findOneByPageId(pageId);
        long version = 1L;
        if (lastVersion != null) {
            version = lastVersion.getVersionNumber() + 1;
        }else{
            lastVersion = new PageLastVersion();
            lastVersion.setPageId(pageId);
        }
        lastVersion.setVersionNumber(version);
        save(lastVersion);
        return version;
    }
}
