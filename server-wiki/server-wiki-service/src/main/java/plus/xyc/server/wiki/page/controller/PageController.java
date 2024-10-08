package plus.xyc.server.wiki.page.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.wiki.page.entity.request.CatalogParentRequest;
import plus.xyc.server.wiki.page.entity.request.CatalogRequest;
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
@RequestMapping("/page")
@Tag(name = "page", description = "页面")
public class PageController {

    @Resource
    private PageService pageService;

    @PostMapping("/create")
    @Operation(summary = "创建页面")
    public CatalogResponse create(
            @RequestBody CreatePageRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setOwnerId(user.getId());
        return pageService.create(request);
    }

    @GetMapping("/catalog")
    @Operation(summary = "目录")
    public List<CatalogResponse> catalog(@ModelAttribute CatalogRequest request) {
        return pageService.catalog(request);
    }

    @PutMapping("/catalog/parent")
    @Operation(summary = "修改父目录")
    public void changeCatalogParent(@RequestBody CatalogParentRequest request) {
        pageService.changeCatalogParent(request);
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "详情")
    public PageDetailResponse detail(@Schema(description = "文章ID") @PathVariable("id") Long id) {
        return pageService.detail(id);
    }

    @PutMapping("/{id}/save")
    @Operation(summary = "更新内容")
    public void saveContent(
            @Schema(description = "文章ID") @PathVariable("id") Long id,
            @RequestBody SavePageContentRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setId(id);
        request.setUpdateUser(user.getId());
        pageService.saveContent(request);
    }

}
