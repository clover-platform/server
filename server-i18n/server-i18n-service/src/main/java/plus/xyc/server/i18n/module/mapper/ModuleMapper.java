package plus.xyc.server.i18n.module.mapper;

import org.apache.ibatis.annotations.Param;
import plus.xyc.server.i18n.module.entity.dto.Module;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.i18n.module.entity.request.ModuleAllRequest;
import plus.xyc.server.i18n.module.entity.request.ModuleQueryRequest;
import plus.xyc.server.i18n.module.entity.response.ModuleResponse;
import plus.xyc.server.i18n.module.entity.response.SizeResponse;

import java.util.List;

/**
 * <p>
 * 项目 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface ModuleMapper extends BaseMapper<Module> {

    List<Module> query(@Param("keyword") String keyword, @Param("query") ModuleQueryRequest query);
    List<SizeResponse> memberSizes(@Param("moduleIds") List<Long> moduleIds);
    List<SizeResponse> targetSizes(@Param("moduleIds") List<Long> moduleIds);
    int countByIdentifier(@Param("identifier") String identifier);
    List<ModuleResponse> all(@Param("request") ModuleAllRequest request);
    Module findOneByIdentifier(@Param("identifier") String identifier);

}
