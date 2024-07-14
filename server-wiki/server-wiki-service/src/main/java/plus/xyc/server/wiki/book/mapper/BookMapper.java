package plus.xyc.server.wiki.book.mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.wiki.book.entity.dto.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.wiki.book.entity.request.BookListRequest;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
public interface BookMapper extends BaseMapper<Book> {

    int countByPathAndDeleted(@Param("path") String path, @Param("deleted") Boolean deleted);
    List<Book> query(Page<Book> page, @Param("request") BookListRequest request);

}
