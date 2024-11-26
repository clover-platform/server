package plus.xyc.server.i18n.bundle.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.i18n.bundle.entity.dto.Bundle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 文件包 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface BundleMapper extends BaseMapper<Bundle> {

    int countByModuleIdAndName(@Param("moduleId") Long moduleId, @Param("name") String name);
    List<Bundle> findByModuleId(@Param("moduleId") Long moduleId);

}
