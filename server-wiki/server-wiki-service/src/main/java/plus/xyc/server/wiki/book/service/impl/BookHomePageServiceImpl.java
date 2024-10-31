package plus.xyc.server.wiki.book.service.impl;

import plus.xyc.server.wiki.book.entity.dto.BookHomePage;
import plus.xyc.server.wiki.book.mapper.BookHomePageMapper;
import plus.xyc.server.wiki.book.service.BookHomePageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-10-31
 */
@Service
public class BookHomePageServiceImpl extends ServiceImpl<BookHomePageMapper, BookHomePage> implements BookHomePageService {

    @Override
    public BookHomePage findByBookId(Long bookId) {
        return baseMapper.findOneByBookId(bookId);
    }
}
