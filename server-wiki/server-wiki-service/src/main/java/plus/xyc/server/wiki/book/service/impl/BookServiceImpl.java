package plus.xyc.server.wiki.book.service.impl;

import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.wiki.book.entity.dto.Book;
import plus.xyc.server.wiki.book.entity.request.CreateBookRequest;
import plus.xyc.server.wiki.book.mapper.BookMapper;
import plus.xyc.server.wiki.book.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
@Service
@Slf4j
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Override
    public void create(CreateBookRequest request) {
        log.info("{}", request);
    }
}
