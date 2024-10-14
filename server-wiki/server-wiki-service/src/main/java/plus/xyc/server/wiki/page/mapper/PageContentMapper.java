package plus.xyc.server.wiki.page.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.wiki.page.entity.dto.PageContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-07-04
 */
public interface PageContentMapper extends BaseMapper<PageContent> {

    PageContent findOneByPageIdAndCurrent(@Param("pageId") Long pageId, @Param("current") Boolean current);

    List<PageContent> selectUpdateUserByPageId(@Param("pageId") Long pageId);
}
