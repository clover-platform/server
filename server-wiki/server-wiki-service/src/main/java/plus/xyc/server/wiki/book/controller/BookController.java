package plus.xyc.server.wiki.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.wiki.access.annotation.MemberAccess;
import plus.xyc.server.wiki.access.enums.AccessCode;
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
@Tag(name = "BookController", description = "知识库")
@Slf4j
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
            @ParameterObject @ModelAttribute PageRequest page,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setKeyword(page.getKeyword());
        request.setUserId(user.getId());
        return bookService.query(page, request);
    }

    @MemberAccess({AccessCode.BOOK_OWNER})
    @DeleteMapping("/{bookPath}")
    @Operation(summary = "删除")
    public void delete(
            @Parameter(description = "访问路径") @PathVariable String bookPath,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        bookService.deleteByPath(bookPath);
    }

    @GetMapping("/{bookPath}")
    @Operation(summary = "详情")
    public BookResponse detail(
            @Parameter(description = "访问路径") @PathVariable String bookPath,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        return bookService.findByPath(bookPath);
    }

}
