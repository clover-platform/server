package plus.xyc.server.wiki.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.rest.MainAccountRestApi;
import plus.xyc.server.wiki.book.entity.request.CreateBookRequest;
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
public class BookController {

    @Resource
    private MainAccountRestApi mainAccountRestApi;
    @Resource
    private BookService bookService;

    @PostMapping("/create")
    @Operation(summary = "创建")
    public void create(
            @RequestBody CreateBookRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        request.setOwnerId(user.getId());
        ApiAccountResponse account = mainAccountRestApi.getById(user.getId()).getData();
        request.setProjectId(account.getCurrentProjectId());
        bookService.create(request);
    }

}
