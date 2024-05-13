package plus.xyc.server.i18n.branch.service.impl;

import plus.xyc.server.i18n.branch.entity.dto.Branch;
import plus.xyc.server.i18n.branch.mapper.BranchMapper;
import plus.xyc.server.i18n.branch.service.BranchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分支 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
public class BranchServiceImpl extends ServiceImpl<BranchMapper, Branch> implements BranchService {

    @Override
    public void createDefault(Long moduleId) {
        Branch branch = new Branch();
        branch.setModuleId(moduleId);
        branch.setName("main");
        branch.setIsDefault(true);
        save(branch);
    }
}
