package plus.xyc.server.i18n.branch.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.i18n.branch.entity.dto.Branch;
import plus.xyc.server.i18n.branch.entity.mapstruct.BranchMapStruct;
import plus.xyc.server.i18n.branch.entity.request.AllBranchRequest;
import plus.xyc.server.i18n.branch.entity.response.BranchResponse;
import plus.xyc.server.i18n.branch.mapper.BranchMapper;
import plus.xyc.server.i18n.branch.service.BranchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 分支 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
@Slf4j
public class BranchServiceImpl extends ServiceImpl<BranchMapper, Branch> implements BranchService {

    @Resource
    private BranchMapStruct branchMapStruct;

    @Override
    public void createDefault(Long moduleId) {
        Branch branch = new Branch();
        branch.setModuleId(moduleId);
        branch.setName("main");
        branch.setIsDefault(true);
        save(branch);
    }

    @Override
    public List<BranchResponse> all(AllBranchRequest request) {
        log.info("all request: {}", request);
        return branchMapStruct.allToBranchResponse(baseMapper.all(request));
    }
}
