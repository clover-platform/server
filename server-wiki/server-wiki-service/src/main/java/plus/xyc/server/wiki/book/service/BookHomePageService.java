package plus.xyc.server.wiki.book.service;

import plus.xyc.server.wiki.book.entity.dto.BookHomePage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generator
 * @since 2024-10-31
 */
public interface BookHomePageService extends IService<BookHomePage> {

    BookHomePage findByBookId(Long bookId);

}
