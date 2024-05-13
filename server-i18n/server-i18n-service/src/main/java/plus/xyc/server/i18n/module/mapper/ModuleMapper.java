package plus.xyc.server.i18n.module.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import plus.xyc.server.i18n.module.entity.dto.Module;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.i18n.module.entity.request.ModuleQueryRequest;
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

    List<Module> query(IPage<Module> page, @Param("keyword") String keyword, @Param("query") ModuleQueryRequest query);
    List<SizeResponse> memberSizes(@Param("moduleIds") List<Long> moduleIds);
    List<SizeResponse> targetSizes(@Param("moduleIds") List<Long> moduleIds);

}
