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
    List<Entry> findByModuleIdAndBranchId(@Param("moduleId") Long moduleId, @Param("branchId") Long branchId);
    List<Entry> query(@Param("keyword") String keyword, @Param("query") EntryListRequest request);
    Long countTotal(@Param("request") EntryCountRequest request);
    List<Entry> findByBranchId(@Param("branchId") Long branchId);
    int countByModuleIdAndBranchIdAndIdentifier(@Param("moduleId") Long moduleId, @Param("branchId") Long branchId, @Param("identifier") String identifier);
    int countByBranchIdAndDeleted(@Param("branchId") Long branchId, @Param("deleted") Boolean deleted);
    List<Entry> findIdByBranchIdAndDeleted(@Param("branchId") Long branchId, @Param("deleted") Boolean deleted);
    int countByModuleIdAndBranchId(@Param("moduleId") Long moduleId, @Param("branchId") Long branchId);

}
