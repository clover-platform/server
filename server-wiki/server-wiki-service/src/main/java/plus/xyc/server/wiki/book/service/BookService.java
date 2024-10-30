package plus.xyc.server.wiki.book.service;

import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.wiki.book.entity.dto.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.wiki.book.entity.request.BookListRequest;
import plus.xyc.server.wiki.book.entity.request.CreateBookRequest;
import plus.xyc.server.wiki.book.entity.response.BookResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
public interface BookService extends IService<Book> {

    void create(CreateBookRequest request);
    PageResult<BookResponse> query(PageQueryRequest page, BookListRequest request);
    Book findByPath(String path);
    void deleteByPath(String path);

}
