package plus.xyc.server.wiki.page.service.impl;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.rest.MainAccountRestApi;
import plus.xyc.server.wiki.page.entity.dto.Page;
import plus.xyc.server.wiki.page.entity.dto.PageContent;
import plus.xyc.server.wiki.page.entity.mapstruct.PageStruct;
import plus.xyc.server.wiki.page.entity.request.CatalogParentRequest;
import plus.xyc.server.wiki.page.entity.request.CreatePageRequest;
import plus.xyc.server.wiki.page.entity.request.SavePageContentRequest;
import plus.xyc.server.wiki.page.entity.response.CatalogResponse;
import plus.xyc.server.wiki.page.entity.response.PageAuthorResponse;
import plus.xyc.server.wiki.page.entity.response.PageDetailResponse;
import plus.xyc.server.wiki.page.mapper.PageCollectMapper;
import plus.xyc.server.wiki.page.mapper.PageContentMapper;
import plus.xyc.server.wiki.page.mapper.PageMapper;
import plus.xyc.server.wiki.page.service.PageCacheService;
import plus.xyc.server.wiki.page.service.PageContentService;
import plus.xyc.server.wiki.page.service.PageLastVersionService;
import plus.xyc.server.wiki.page.service.PageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
    @Resource
    private PageCollectMapper pageCollectMapper;
    @Resource
    private MainAccountRestApi mainAccountRestApi;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private PageCacheService pageCacheService;

    @Override
    @Transactional
    public CatalogResponse create(CreatePageRequest request) {
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

        return pageStruct.toCatalogResponse(page);
    }

    @Override
    public List<CatalogResponse> catalog(Long bookId) {
        List<Page> all = getBaseMapper().findByBookIdAndDeleted(bookId, false);
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
    public PageDetailResponse detail(Long id, Long currentUserId) {
        String key = "wiki:book:page:" + id;
        if(currentUserId != null) {
            key = "wiki:book:page:" + id + ":" + currentUserId;
        }
        Object cache = redisTemplate.opsForValue().get(key);
        if(cache != null) {
            return (PageDetailResponse) cache;
        }
        Page page = getById(id);
        PageDetailResponse response = pageStruct.toPageDetailResponse(page);
        PageContent content = pageContentMapper.findOneByPageIdAndCurrent(id, true);
        BeanUtils.copyProperties(content, response);
        if(currentUserId != null) { // 是否已收藏
            response.setCollected(pageCollectMapper.countByPageIdAndUserId(id, currentUserId) > 0);
        }
        List<Long> userIdList = pageContentService.getHistoryUserList(id);
        if(!userIdList.contains(page.getOwner())) {
            userIdList.add(page.getOwner());
        }
        ApiAccountListRequest request = new ApiAccountListRequest();
        request.setIds(userIdList);
        Result<PageResult<ApiAccountResponse>> result =  mainAccountRestApi.list(request);
        if(!result.isSuccess()) {
            throw ResultException.internal();
        }
        List<PageAuthorResponse> authorList = result.getData().getData().stream()
                .map(account -> {
                    PageAuthorResponse author = new PageAuthorResponse();
                    author.setAccount(account);
                    author.setUserId(account.getId());
                    author.setOwner(page.getOwner().equals(account.getId()));
                    return author;
                })
                .toList();
        response.setUserList(authorList);
        log.info("detail {}", response);
        redisTemplate.opsForValue().set(key, response, 1, TimeUnit.HOURS);
        return response;
    }

    @Override
    @Transactional
    public Long saveContent(SavePageContentRequest request) {
        // 清空缓存
        pageCacheService.clearCache(request.getId());
        // 更新标题
        getBaseMapper().updateTitleById(request.getTitle(), request.getId());
        Long lastVersion = pageLastVersionService.getLastVersion(request.getId());
        if(request.getNewVersion()) {
            // 保存新版本
            PageContent content = pageContentMapper.findOneByPageIdAndCurrent(request.getId(), true);
            if(request.getContent() == null) {
                return lastVersion;
            }
            if(request.getContent().equals(content.getContent())) {
                return lastVersion;
            }
            Long newPageId = pageContentService.newVersion(request.getId(), request.getUpdateUser(), request.getContent());
            // 其他的重置为非当前版本
            pageContentService.resetCurrent(request.getId(), newPageId);
            return lastVersion + 1;
        }else{
            pageContentService.updateContent(request.getId(), request.getUpdateUser(), request.getContent());
        }
        return lastVersion;
    }

}
