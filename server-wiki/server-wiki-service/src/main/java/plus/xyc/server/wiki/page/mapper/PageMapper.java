package plus.xyc.server.wiki.page.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.wiki.page.entity.dto.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 目录 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
public interface PageMapper extends BaseMapper<Page> {

    List<Page> findByBookIdAndDeleted(@Param("bookId") Long bookId, @Param("deleted") Boolean deleted);

}
