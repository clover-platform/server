package plus.xyc.server.wiki.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.rest.MainAccountRestApi;
import plus.xyc.server.wiki.book.entity.dto.Book;
import plus.xyc.server.wiki.book.entity.dto.BookHomePage;
import plus.xyc.server.wiki.book.entity.dto.BookMember;
import plus.xyc.server.wiki.book.entity.mapstruct.BookMapStruct;
import plus.xyc.server.wiki.book.entity.request.BookListRequest;
import plus.xyc.server.wiki.book.entity.request.CreateBookRequest;
import plus.xyc.server.wiki.book.entity.response.BookResponse;
import plus.xyc.server.wiki.book.mapper.BookMapper;
import plus.xyc.server.wiki.book.service.BookHomePageService;
import plus.xyc.server.wiki.book.service.BookMemberService;
import plus.xyc.server.wiki.book.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.wiki.enums.WikiCode;

import java.util.Date;
import java.util.List;

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

    @Resource
    private MainAccountRestApi mainAccountRestApi;
    @Resource
    private BookMapStruct struct;
    @Resource
    private BookMemberService bookMemberService;
    @Resource
    private BookHomePageService bookHomePageService;

    @Override
    @DistributedLock(value = "wiki:book:create", el = false)
    @Transactional
    public void create(CreateBookRequest request) {
        int size = baseMapper.countByPathAndDeleted(request.getPath(), false);
        if(size > 0) {
            throw new ResultException(WikiCode.BOOK_PATH_EXISTED.code, MessageUtils.get(WikiCode.BOOK_PATH_EXISTED.key));
        }

        ApiAccountResponse account = mainAccountRestApi.getById(request.getOwnerId()).getData();
        Book book = struct.toBook(request);
        book.setProjectId(account.getCurrentProjectId());
        log.info("book: {}", book);
        Date now = new Date();
        book.setCreateTime(now);
        book.setUpdateTime(now);
        save(book);

        // 成员
        BookMember owner = new BookMember();
        owner.setBookId(book.getId());
        owner.setUserId(request.getOwnerId());
        owner.setRole(1);
        bookMemberService.save(owner);
    }

    @Override
    public PageResult<BookResponse> query(PageQueryRequest pr, BookListRequest request) {
        log.info("page: {}, request: {}", pr, request);
        Page<Book> page = pr.toPage();
        List<Book> books = baseMapper.query(page, request);
        return PageResult.of(page.getTotal(), books.stream().map(struct::toResponse).toList());
    }

    @Override
    @Cacheable(value = "wiki:book:base", key = "#path")
    public Book findBookByPath(String path) {
        return baseMapper.findOneByPathAndDeleted(path, false);
    }

    @Override
    @Cacheable(value = "wiki:book", key = "#path")
    public BookResponse findByPath(String path) {
        Book book = ((BookService) AopContext.currentProxy()).findBookByPath(path);
        BookResponse response = struct.toResponse(book);
        BookHomePage homePage = bookHomePageService.findByBookId(response.getId());
        response.setHomePage(homePage);
        return response;
    }

    @Override
    @CacheEvict(value = "wiki:book:base", key = "#path")
    public void deleteByPath(String path) {
        UpdateWrapper<Book> wrapper = new UpdateWrapper<>();
        wrapper.eq("path", path);
        wrapper.set("deleted", true);
        update(wrapper);
    }
}
