package plus.xyc.server.wiki.page.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.zkit.support.starter.boot.utils.MessageUtils;
import plus.xyc.server.wiki.page.entity.dto.Page;
import plus.xyc.server.wiki.page.entity.dto.PageContent;
import plus.xyc.server.wiki.page.entity.mapstruct.PageContentStruct;
import plus.xyc.server.wiki.page.entity.mapstruct.PageStruct;
import plus.xyc.server.wiki.page.entity.request.CatalogParentRequest;
import plus.xyc.server.wiki.page.entity.request.CatalogRequest;
import plus.xyc.server.wiki.page.entity.request.CreatePageRequest;
import plus.xyc.server.wiki.page.entity.request.SavePageContentRequest;
import plus.xyc.server.wiki.page.entity.response.CatalogResponse;
import plus.xyc.server.wiki.page.entity.response.PageDetailResponse;
import plus.xyc.server.wiki.page.mapper.PageContentMapper;
import plus.xyc.server.wiki.page.mapper.PageMapper;
import plus.xyc.server.wiki.page.service.PageContentService;
import plus.xyc.server.wiki.page.service.PageLastVersionService;
import plus.xyc.server.wiki.page.service.PageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 目录 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
@Service
@Slf4j
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {

    @Resource
    private PageContentService pageContentService;
    @Resource
    private PageLastVersionService pageLastVersionService;
    @Resource
    private PageStruct pageStruct;
    @Resource
    private PageContentMapper pageContentMapper;

    @Override
    @Transactional
    public Long create(CreatePageRequest request) {
        String title = MessageUtils.get("page.title.default");
        Page page = new Page();
        page.setTitle(title);
        page.setBookId(request.getBookId());
        page.setOwner(request.getOwnerId());
        page.setTitle(title);
        page.setParentId(request.getParent());
        save(page);

        // 获得最新版本号
        Long version = pageLastVersionService.getVersion(page.getId());

        // 存储内容
        PageContent pageContent = new PageContent();
        pageContent.setPageId(page.getId());
        pageContent.setVersionNumber(version);
        pageContent.setUpdateUser(request.getOwnerId());
        pageContent.setCurrent(true);
        pageContentService.save(pageContent);

        return page.getId();
    }

    @Override
    public List<CatalogResponse> catalog(CatalogRequest request) {
        List<Page> all = getBaseMapper().findByBookIdAndDeleted(request.getBookId(), false);
        return all.stream()
                .filter(page -> page.getParentId() == null)
                .map(page -> {
                    CatalogResponse catalog = pageStruct.toCatalogResponse(page);
                    setChildren(catalog, all);
                    return catalog;
                })
                .toList();
    }

    private void setChildren(CatalogResponse catalog, List<Page> all) {
        List<CatalogResponse> children = all.stream()
                .filter(page -> page.getParentId() != null)
                .filter(page -> page.getParentId().equals(catalog.getId()))
                .map(pageStruct::toCatalogResponse)
                .toList();
        catalog.setChildren(children);
        children.forEach(child -> setChildren(child, all));
    }

    @Override
    public void changeCatalogParent(CatalogParentRequest request) {
        log.info("changeCatalogParent {}", request);
        getBaseMapper().updateParentIdById(request.getParentId(), request.getId());
    }

    @Override
    public PageDetailResponse detail(Long id) {
        Page page = getById(id);
        PageDetailResponse response = pageStruct.toPageDetailResponse(page);
        PageContent content = pageContentMapper.findOneByPageIdAndCurrent(id, true);
        BeanUtils.copyProperties(content, response);
        log.info("detail {}", response);
        return response;
    }

    @Override
    @Transactional
    public void saveContent(SavePageContentRequest request) {
        PageDetailResponse response = detail(request.getId());
        if(request.getContent() == null) {
            return;
        }
        if(request.getTitle().equals(response.getContent())) {
            return;
        }
        // 更新标题
        getBaseMapper().updateTitleById(request.getTitle(), request.getId());
        // 保存新版本
        Long newPageId = pageContentService.newVersion(request.getId(), request.getUpdateUser(), request.getContent());
        // 其他的重置为非当前版本
        pageContentService.resetCurrent(request.getId(), newPageId);
    }

}
