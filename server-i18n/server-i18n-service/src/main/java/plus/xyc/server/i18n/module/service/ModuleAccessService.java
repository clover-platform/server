package plus.xyc.server.i18n.module.service;

import java.util.List;

public interface ModuleAccessService {

    boolean check(Long moduleId, Long userId, List<Integer> needRoles);

}
