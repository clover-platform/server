package plus.xyc.server.wiki.book.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.wiki.book.entity.dto.BookMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
public interface BookMemberMapper extends BaseMapper<BookMember> {

    List<BookMember> findUserIdByBookId(@Param("bookId") Long bookId);

}
