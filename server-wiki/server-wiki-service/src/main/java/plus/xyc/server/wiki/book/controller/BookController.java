package plus.xyc.server.wiki.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.wiki.book.entity.request.BookListRequest;
import plus.xyc.server.wiki.book.entity.request.CreateBookRequest;
import plus.xyc.server.wiki.book.entity.response.BookResponse;
import plus.xyc.server.wiki.book.service.BookService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
@RestController
@RequestMapping("/book")
@Tag(name = "book", description = "知识库")
public class BookController {

    @Resource
    private BookService bookService;

    @PostMapping("/create")
    @Operation(summary = "创建")
    public void create(
            @RequestBody CreateBookRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setOwnerId(user.getId());
        bookService.create(request);
    }

    @GetMapping("/list")
    @Operation(summary = "创建")
    public PageResult<BookResponse> list(
            @ParameterObject @ModelAttribute BookListRequest request,
            @ParameterObject @ModelAttribute PageQueryRequest page,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setKeyword(page.getKeyword());
        request.setUserId(user.getId());
        return bookService.query(page, request);
    }

}
