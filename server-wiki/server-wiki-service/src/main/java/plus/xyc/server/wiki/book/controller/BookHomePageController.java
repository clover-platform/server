package plus.xyc.server.wiki.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.wiki.book.annotation.BookInject;
import plus.xyc.server.wiki.book.entity.dto.Book;
import plus.xyc.server.wiki.book.entity.request.BookHomePageSaveRequest;
import plus.xyc.server.wiki.book.service.BookHomePageService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-10-31
 */
@RestController
@RequestMapping("/book/{bookPath}/home/page")
@Tag(name = "BookHomePageController", description = "知识库首页")
public class BookHomePageController {

    @Resource
    private BookHomePageService bookHomePageService;

    @PutMapping("/save")
    @Operation(summary = "保存首页")
    public void save(
            @Parameter(description = "访问路径") @PathVariable String bookPath,
            @CurrentUser @Parameter(hidden = true) SessionUser user,
            @BookInject Book book,
            @RequestBody BookHomePageSaveRequest request
    ) {
        request.setBookPath(bookPath);
        request.setBookId(book.getId());
        request.setUpdateUserId(user.getId());
        bookHomePageService.save(request);
    }

}
