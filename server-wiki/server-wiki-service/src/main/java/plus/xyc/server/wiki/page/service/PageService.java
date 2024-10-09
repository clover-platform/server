package plus.xyc.server.wiki.page.service;

import plus.xyc.server.wiki.page.entity.dto.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.wiki.page.entity.request.CatalogParentRequest;
import plus.xyc.server.wiki.page.entity.request.CreatePageRequest;
import plus.xyc.server.wiki.page.entity.request.SavePageContentRequest;
import plus.xyc.server.wiki.page.entity.response.CatalogResponse;
import plus.xyc.server.wiki.page.entity.response.PageDetailResponse;

import java.util.List;

/**
 * <p>
 * 目录 服务类
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
public interface PageService extends IService<Page> {

    CatalogResponse create(CreatePageRequest request);
    List<CatalogResponse> catalog(Long bookId);
    void changeCatalogParent(CatalogParentRequest request);
    PageDetailResponse detail(Long id);
    void saveContent(SavePageContentRequest request);

}
