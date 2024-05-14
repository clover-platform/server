package plus.xyc.server.i18n.entry.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.i18n.entry.entity.dto.Entry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 词条 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface EntryMapper extends BaseMapper<Entry> {

    List<Entry> findByModuleId(@Param("moduleId") Long moduleId);

}
