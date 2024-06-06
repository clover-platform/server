package plus.xyc.server.i18n.activity.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import plus.xyc.server.i18n.activity.entity.dto.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import plus.xyc.server.i18n.activity.entity.request.ActivityListRequest;

import java.util.List;

/**
 * <p>
 * 项目动态 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface ActivityMapper extends BaseMapper<Activity> {

    List<Activity> query(IPage<Activity> page, @Param("request")ActivityListRequest request);

}
