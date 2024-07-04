package plus.xyc.server.wiki.book.service;

import plus.xyc.server.wiki.book.entity.dto.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.wiki.book.entity.request.CreateBookRequest;

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

}
