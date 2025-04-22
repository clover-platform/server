package plus.xyc.server.wiki.book.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.wiki.book.entity.dto.BookHomePage;
import plus.xyc.server.wiki.book.entity.request.BookHomePageSaveRequest;
import plus.xyc.server.wiki.book.mapper.BookHomePageMapper;
import plus.xyc.server.wiki.book.service.BookHomePageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Override
    @Transactional
    @DistributedLock(value = "'wiki:book:home:page:' +#request.bookId")
    @CacheEvict(value = "wiki:book", key = "#request.bookPath")
    public void save(BookHomePageSaveRequest request) {
        BookHomePage page = findByBookId(request.getBookId());
        if(page == null) {
            page = new BookHomePage();
            page.setBookId(request.getBookId());
        }
        page.setContent(request.getContent());
        page.setUpdateUser(request.getUpdateUserId());
        page.setUpdateTime(new Date());
        saveOrUpdate(page);
    }

}
