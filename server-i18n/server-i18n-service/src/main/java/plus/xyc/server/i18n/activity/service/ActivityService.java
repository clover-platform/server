package plus.xyc.server.i18n.activity.service;

import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.activity.entity.dto.Activity;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.activity.entity.request.ActivityListRequest;

/**
 * <p>
 * 项目动态 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface ActivityService extends IService<Activity> {

    void module(Long id, Integer operate, Object origin);
    void file(Long id, Integer operate, Object origin);
    void bundle(Long id, Integer operate, Object origin);
    void member(Long id, Integer operate, Object origin);
    void setting(Long id, Integer operate, Object origin);
    void entity(Long id, Integer subType, Integer operate, Object origin);
    void log(Long id, Integer type, Integer subType, Integer operate, Object origin);
    PageResult<Activity> query(PageRequest page, ActivityListRequest request);

}
