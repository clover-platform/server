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
import plus.xyc.server.wiki.page.entity.request.CatalogParentRequest;
import plus.xyc.server.wiki.page.entity.request.CreatePageRequest;
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
@RequestMapping("/book/{bookId}/page")
@Tag(name = "page", description = "页面")
public class PageController {

    @Resource
    private PageService pageService;

    @PostMapping("/create")
    @Operation(summary = "创建页面")
    public CatalogResponse create(
            @Schema(description = "知识库ID") @PathVariable("bookId") Long bookId,
            @RequestBody CreatePageRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setBookId(bookId);
        request.setOwnerId(user.getId());
        return pageService.create(request);
    }

    @GetMapping("/catalog")
    @Operation(summary = "目录")
    public List<CatalogResponse> catalog(@Schema(description = "知识库ID") @PathVariable("bookId") Long bookId) {
        return pageService.catalog(bookId);
    }

    @PutMapping("/{pageId}/parent")
    @Operation(summary = "修改父目录")
    public void changeCatalogParent(
            @Schema(description = "知识库ID") @PathVariable("bookId") Long bookId,
            @Schema(description = "页面ID") @PathVariable("pageId") Long pageId,
            @RequestBody CatalogParentRequest request
    ) {
        request.setBookId(bookId);
        request.setId(pageId);
        pageService.changeCatalogParent(request);
    }

    @MemberAccess()
    @GetMapping("/{pageId}")
    @Operation(summary = "详情")
    public PageDetailResponse detail(
            @Schema(description = "知识库ID") @PathVariable("bookId") Long bookId,
            @Schema(description = "文章ID") @PathVariable("pageId") Long pageId,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        return pageService.detail(pageId, user.getId());
    }

    @PutMapping("/{pageId}")
    @Operation(summary = "更新内容")
    public Long saveContent(
            @Schema(description = "知识库ID") @PathVariable("bookId") Long bookId,
            @Schema(description = "文章ID") @PathVariable("pageId") Long pageId,
            @RequestBody SavePageContentRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setId(pageId);
        request.setUpdateUser(user.getId());
        return pageService.saveContent(request);
    }

}
