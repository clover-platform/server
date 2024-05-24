package plus.xyc.server.i18n.entry.mapper;

import org.apache.ibatis.annotations.Param;
import plus.xyc.server.i18n.entry.entity.dto.EntryState;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;

/**
 * <p>
 * 词条翻译状态 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-24
 */
public interface EntryStateMapper extends BaseMapper<EntryState> {

    Long countTranslated(@Param("request") EntryCountRequest request);
    Long countVerified(@Param("request") EntryCountRequest request);

}
