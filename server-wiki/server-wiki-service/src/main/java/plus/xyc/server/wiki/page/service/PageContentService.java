package plus.xyc.server.wiki.page.service;

import plus.xyc.server.wiki.page.entity.dto.PageContent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
public interface PageContentService extends IService<PageContent> {

    void resetCurrent(Long pageId, Long contentId);
    Long newVersion(Long pageId, Long updateUser, String content);
    void updateContent(Long pageId, Long updateUser, String content);
    List<Long> getHistoryUserList(Long pageId);

}
