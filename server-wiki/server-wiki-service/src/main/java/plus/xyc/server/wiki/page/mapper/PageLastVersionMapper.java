package plus.xyc.server.wiki.page.mapper;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.wiki.page.entity.dto.PageLastVersion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-07-28
 */
public interface PageLastVersionMapper extends BaseMapper<PageLastVersion> {

    PageLastVersion findOneByPageId(@Param("pageId") Long pageId);

}
