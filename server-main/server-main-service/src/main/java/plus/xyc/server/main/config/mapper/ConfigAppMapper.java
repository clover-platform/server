package plus.xyc.server.main.config.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import plus.xyc.server.main.config.entity.dto.ConfigApp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 应用 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2025-02-20
 */
public interface ConfigAppMapper extends BaseMapper<ConfigApp> {

    List<ConfigApp> findByEnable(@Param("enable") Boolean enable);

}
