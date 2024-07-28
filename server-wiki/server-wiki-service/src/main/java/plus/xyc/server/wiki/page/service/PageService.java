package plus.xyc.server.wiki.page.service;

import plus.xyc.server.wiki.page.entity.dto.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.wiki.page.entity.request.CatalogRequest;
import plus.xyc.server.wiki.page.entity.request.CreatePageRequest;
import plus.xyc.server.wiki.page.entity.response.CatalogResponse;

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

    Long create(CreatePageRequest request);
    List<CatalogResponse> catalog(CatalogRequest request);

}
