package plus.xyc.server.i18n.module.mapper;
import java.util.Collection;

import org.apache.ibatis.annotations.Param;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.module.entity.dto.ModuleCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 模块统计 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-24
 */
public interface ModuleCountMapper extends BaseMapper<ModuleCount> {

    List<ModuleCount> findByCountRequest(@Param("request")EntryCountRequest request);
    ModuleCount findOneByModuleIdAndBranchIdAndCode(@Param("moduleId") Long moduleId, @Param("branchId") Long branchId, @Param("code") String code);
    List<ModuleCount> findByModuleId(@Param("moduleId") Long moduleId);
    List<ModuleCount> findByModuleIdIn(@Param("moduleIdList") Collection<Long> moduleIdList);

}
