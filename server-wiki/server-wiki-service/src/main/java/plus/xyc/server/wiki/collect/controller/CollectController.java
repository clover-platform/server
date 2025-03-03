package plus.xyc.server.wiki.collect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.wiki.book.annotation.BookInject;
import plus.xyc.server.wiki.book.entity.dto.Book;
import plus.xyc.server.wiki.collect.entity.request.CollectRequest;
import plus.xyc.server.wiki.collect.service.CollectService;

/**
 * <p>
 * 收藏夹 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-10-14
 */
@RestController
@RequestMapping("/book/{bookPath}")
@Tag(name = "CollectController", description = "收藏")
public class CollectController {

    @Resource
    private CollectService collectService;

    @PostMapping("/collect")
    @Operation(summary = "收藏页面")
    public void bookCollect(
            @Schema(description = "知识库ID") @PathVariable("bookPath") String bookPath,
            @BookInject Book book,
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @RequestBody CollectRequest request
    ) {
        request.setUserId(user.getId());
        request.setBookId(book.getId());
        collectService.collect(request);
    }

    @PostMapping("/page/{pageId}/collect")
    @Operation(summary = "收藏页面")
    public void pageCollect(
            @Schema(description = "知识库ID") @PathVariable("bookPath") String bookPath,
            @Schema(description = "文章ID") @PathVariable("pageId") Long pageId,
            @BookInject Book book,
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @RequestBody CollectRequest request
    ) {
        request.setUserId(user.getId());
        request.setBookId(book.getId());
        request.setPageId(pageId);
        collectService.collect(request);
    }

}
