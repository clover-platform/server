package plus.xyc.server.wiki.page.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.wiki.access.annotation.MemberAccess;
import plus.xyc.server.wiki.book.annotation.BookInject;
import plus.xyc.server.wiki.book.entity.dto.Book;
import plus.xyc.server.wiki.page.entity.request.CatalogParentRequest;
import plus.xyc.server.wiki.page.entity.request.CreatePageRequest;
import plus.xyc.server.wiki.page.entity.request.DeletePageRequest;
import plus.xyc.server.wiki.page.entity.request.SavePageContentRequest;
import plus.xyc.server.wiki.page.entity.response.CatalogResponse;
import plus.xyc.server.wiki.page.entity.response.PageDetailResponse;
import plus.xyc.server.wiki.page.service.PageService;

import java.util.List;

/**
 * <p>
 * 目录 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
@RestController
@RequestMapping("/book/{bookPath}/page")
@Tag(name = "page", description = "页面")
public class PageController {

    @Resource
    private PageService pageService;

    @PostMapping("/create")
    @Operation(summary = "创建页面")
    public CatalogResponse create(
            @Schema(description = "知识库ID") @PathVariable("bookPath") String bookPath,
            @BookInject Book book,
            @RequestBody CreatePageRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setBookId(book.getId());
        request.setOwnerId(user.getId());
        return pageService.create(request);
    }

    @GetMapping("/catalog")
    @Operation(summary = "目录")
    public List<CatalogResponse> catalog(
            @Schema(description = "知识库ID") @PathVariable("bookPath") String bookPath,
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @BookInject Book book
    ) {
        return pageService.catalog(book.getId(), user.getId());
    }

    @PutMapping("/{pageId}/parent")
    @Operation(summary = "修改父目录")
    public void changeCatalogParent(
            @Schema(description = "知识库ID") @PathVariable("bookPath") String bookPath,
            @Schema(description = "页面ID") @PathVariable("pageId") Long pageId,
            @BookInject Book book,
            @RequestBody CatalogParentRequest request
    ) {
        request.setBookId(book.getId());
        request.setId(pageId);
        pageService.changeCatalogParent(request);
    }

    @MemberAccess()
    @GetMapping("/{pageId}")
    @Operation(summary = "详情")
    public PageDetailResponse detail(
            @Schema(description = "知识库ID") @PathVariable("bookPath") String bookPath,
            @Schema(description = "文章ID") @PathVariable("pageId") Long pageId,
            @BookInject Book book,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        return pageService.detail(pageId, user.getId());
    }

    @PutMapping("/{pageId}")
    @Operation(summary = "更新内容")
    public Long saveContent(
            @Schema(description = "知识库ID") @PathVariable("bookPath") String bookPath,
            @Schema(description = "文章ID") @PathVariable("pageId") Long pageId,
            @BookInject Book book,
            @RequestBody SavePageContentRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setId(pageId);
        request.setUpdateUser(user.getId());
        return pageService.saveContent(request);
    }

    @DeleteMapping("/{pageId}")
    @Operation(summary = "删除页面")
    public void delete(
            @Schema(description = "知识库ID") @PathVariable("bookPath") String bookPath,
            @Schema(description = "文章ID") @PathVariable("pageId") Long pageId,
            @BookInject Book book,
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @RequestBody DeletePageRequest request
    ) {
        request.setPageId(pageId);
        request.setBookId(book.getId());
        request.setUserId(user.getId());
        pageService.delete(request);
    }

}
