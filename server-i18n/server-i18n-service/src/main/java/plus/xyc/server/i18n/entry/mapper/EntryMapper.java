package plus.xyc.server.i18n.entry.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import plus.xyc.server.i18n.entry.entity.dto.Entry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryListRequest;

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
    List<Entry> findByModuleIdAndFileId(@Param("moduleId") Long moduleId, @Param("fileId") Long fileId);
    List<Entry> query(@Param("keyword") String keyword, @Param("query") EntryListRequest request);
    Long countTotal(@Param("request") EntryCountRequest request);
    List<Entry> findByFileId(@Param("fileId") Long fileId); 
    int countByModuleIdAndFileIdAndIdentifier(@Param("moduleId") Long moduleId, @Param("fileId") Long fileId, @Param("identifier") String identifier);
    int countByFileIdAndDeleted(@Param("fileId") Long fileId, @Param("deleted") Boolean deleted);
    List<Entry> findIdByFileIdAndDeleted(@Param("fileId") Long fileId, @Param("deleted") Boolean deleted);
    int countByModuleIdAndFileId(@Param("moduleId") Long moduleId, @Param("fileId") Long fileId);

}
