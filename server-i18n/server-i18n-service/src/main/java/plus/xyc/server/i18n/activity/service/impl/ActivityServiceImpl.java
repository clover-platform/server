package plus.xyc.server.i18n.activity.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.security.SessionHolder;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.activity.entity.dto.Activity;
import plus.xyc.server.i18n.activity.entity.enums.ActivityType;
import plus.xyc.server.i18n.activity.entity.request.ActivityListRequest;
import plus.xyc.server.i18n.activity.mapper.ActivityMapper;
import plus.xyc.server.i18n.activity.service.ActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public PageResult<Activity> query(PageQueryRequest pageRequest, ActivityListRequest request) {
        Page<Activity> page = pageRequest.toPage();
        List<Activity> modules = baseMapper.query(page, request);
        return PageResult.of(page.getTotal(), modules);
    }
}
