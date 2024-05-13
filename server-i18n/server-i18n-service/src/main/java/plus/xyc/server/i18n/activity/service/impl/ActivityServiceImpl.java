package plus.xyc.server.i18n.activity.service.impl;

import org.zkit.support.starter.boot.auth.SessionHolder;
import org.zkit.support.starter.boot.entity.SessionUser;
import plus.xyc.server.i18n.activity.entity.dto.Activity;
import plus.xyc.server.i18n.activity.entity.enums.ActivityType;
import plus.xyc.server.i18n.activity.mapper.ActivityMapper;
import plus.xyc.server.i18n.activity.service.ActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目动态 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Override
    public void module(Long id, Integer operate, Object origin) {
        log(id, ActivityType.MODULE.code, null, operate, origin);
    }

    @Override
    public void branch(Long id, Integer operate, Object origin) {
        log(id, ActivityType.BRANCH.code, null, operate, origin);
    }

    @Override
    public void bundle(Long id, Integer operate, Object origin) {
        log(id, ActivityType.BUNDLE.code, null, operate, origin);
    }

    @Override
    public void member(Long id, Integer operate, Object origin) {
        log(id, ActivityType.MEMBER.code, null, operate, origin);
    }

    @Override
    public void setting(Long id, Integer operate, Object origin) {
        log(id, ActivityType.SETTING.code, null, operate, origin);
    }

    @Override
    public void entity(Long id, Integer subType, Integer operate, Object origin) {
        log(id, ActivityType.ENTRY.code, subType, operate, origin);
    }

    @Override
    public void log(Long id, Integer type, Integer subType, Integer operate, Object origin) {
        SessionUser user = SessionHolder.get();
        Activity activity = new Activity();
        activity.setModuleId(id);
        activity.setUserId(user.getId());
        activity.setType(type);
        activity.setSubType(subType);
        activity.setOperate(operate);
        activity.setDetail(origin);
        save(activity);
    }
}
