package plus.xyc.server.wiki.page.controller;

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
import plus.xyc.server.wiki.page.entity.request.CollectPageRequest;
import plus.xyc.server.wiki.page.service.PageCollectService;

/**
 * <p>
 * 收藏夹 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-10-14
 */
@RestController
@RequestMapping("/book/{bookPath}/page")
@Tag(name = "page", description = "收藏")
public class PageCollectController {

    @Resource
    private PageCollectService pageCollectService;

    @PostMapping("/{pageId}/collect")
    @Operation(summary = "收藏页面")
    public void collect(
            @Schema(description = "知识库ID") @PathVariable("bookPath") String bookPath,
            @Schema(description = "文章ID") @PathVariable("pageId") Long pageId,
            @BookInject Book book,
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @RequestBody CollectPageRequest request
    ) {
        request.setUserId(user.getId());
        request.setId(pageId);
        pageCollectService.collect(request);
    }

}
