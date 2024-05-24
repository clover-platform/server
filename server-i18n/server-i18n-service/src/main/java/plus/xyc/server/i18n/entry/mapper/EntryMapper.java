package plus.xyc.server.i18n.entry.mapper;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.i18n.entry.entity.dto.Entry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCountResponse;
import plus.xyc.server.i18n.module.entity.dto.Module;

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
    List<Entry> query(IPage<Entry> page, @Param("keyword") String keyword, @Param("query") EntryListRequest request);
    Long countTotal(@Param("request") EntryCountRequest request);
    List<Entry> findByBranchId(@Param("branchId") Long branchId);
    int countByModuleIdAndBranchIdAndIdentifier(@Param("moduleId") Long moduleId, @Param("branchId") Long branchId, @Param("identifier") String identifier);

}
