package plus.xyc.server.wiki.book.mapper;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.wiki.book.entity.dto.BookHomePage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-10-31
 */
public interface BookHomePageMapper extends BaseMapper<BookHomePage> {

    BookHomePage findOneByBookId(@Param("bookId") Long bookId);

}
