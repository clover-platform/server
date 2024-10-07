package plus.xyc.server.wiki.page.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import plus.xyc.server.wiki.page.entity.dto.PageContent;
import plus.xyc.server.wiki.page.mapper.PageContentMapper;
import plus.xyc.server.wiki.page.service.PageContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.wiki.page.service.PageLastVersionService;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
@Service
public class PageContentServiceImpl extends ServiceImpl<PageContentMapper, PageContent> implements PageContentService {

    @Resource
    private PageLastVersionService pageLastVersionService;

    @Override
    public void resetCurrent(Long pageId, Long contentId) {
        Wrapper<PageContent> update = new UpdateWrapper<PageContent>()
                .set("current", false)
                .eq("page_id", pageId)
                .ne("id", contentId);
        getBaseMapper().update(update);
    }

    @Override
    public Long newVersion(Long pageId, Long updateUser, String content) {
        Long lastVersion = pageLastVersionService.getVersion(pageId);
        PageContent pc = new PageContent();
        pc.setPageId(pageId);
        pc.setVersionNumber(lastVersion);
        pc.setContent(content);
        pc.setCurrent(true);
        pc.setUpdateUser(updateUser);
        pc.setUpdateTime(new Date());
        save(pc);
        return pc.getId();
    }
}
