package plus.xyc.server.i18n.activity.service;

import plus.xyc.server.i18n.activity.entity.dto.Activity;
import com.baomidou.mybatisplus.extension.service.IService;

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
    void branch(Long id, Integer operate, Object origin);
    void bundle(Long id, Integer operate, Object origin);
    void member(Long id, Integer operate, Object origin);
    void setting(Long id, Integer operate, Object origin);
    void entity(Long id, Integer subType, Integer operate, Object origin);
    void log(Long id, Integer type, Integer subType, Integer operate, Object origin);

}
