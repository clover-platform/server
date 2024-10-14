package plus.xyc.server.wiki.page.mapper;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.wiki.page.entity.dto.PageCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 收藏夹 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-10-14
 */
public interface PageCollectMapper extends BaseMapper<PageCollect> {

    int countByPageIdAndUserId(@Param("pageId") Long pageId, @Param("userId") Long userId);

}
