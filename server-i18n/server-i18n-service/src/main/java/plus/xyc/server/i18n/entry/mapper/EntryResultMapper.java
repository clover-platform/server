package plus.xyc.server.i18n.entry.mapper;

import org.apache.ibatis.annotations.Param;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.i18n.entry.entity.request.EntryResultListRequest;

import java.util.List;

/**
 * <p>
 * 翻译结果 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface EntryResultMapper extends BaseMapper<EntryResult> {

    EntryResult getLastResult(@Param("entryId") Long entryId, @Param("language") String language);
    List<EntryResult> getLastResults(@Param("ids") List<Long> ids, @Param("language") String language);
    List<EntryResult> getResults(@Param("ids") List<Long> ids, @Param("language") String language);
    List<EntryResult> query(@Param("request") EntryResultListRequest request);
    List<EntryResult> findByEntryIds(@Param("entryIds") List<Long> ids);
    EntryResult findOneByEntryIdAndLanguageAndContent(@Param("entryId") Long entryId, @Param("language") String language, @Param("content") String content);

}
