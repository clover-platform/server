package plus.xyc.server.wiki.collect.mapper;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.wiki.collect.entity.dto.Collect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 收藏夹 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-11-01
 */
public interface CollectMapper extends BaseMapper<Collect> {

    int countByBookIdAndPageIdAndUserId(@Param("bookId") Long bookId, @Param("pageId") Long pageId, @Param("userId") Long userId);
    int deleteByBookIdAndPageIdAndUserId(@Param("bookId") Long bookId, @Param("pageId") Long pageId, @Param("userId") Long userId);

}
