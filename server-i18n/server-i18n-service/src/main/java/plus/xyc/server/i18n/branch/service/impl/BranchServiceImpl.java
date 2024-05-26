package plus.xyc.server.i18n.branch.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.activity.entity.enums.ActivityOperate;
import plus.xyc.server.i18n.activity.service.ActivityService;
import plus.xyc.server.i18n.branch.entity.dto.Branch;
import plus.xyc.server.i18n.branch.entity.mapstruct.BranchMapStruct;
import plus.xyc.server.i18n.branch.entity.request.BranchAllRequest;
import plus.xyc.server.i18n.branch.entity.request.BranchCreateRequest;
import plus.xyc.server.i18n.branch.entity.request.BranchListRequest;
import plus.xyc.server.i18n.branch.entity.response.BranchResponse;
import plus.xyc.server.i18n.branch.mapper.BranchMapper;
import plus.xyc.server.i18n.branch.service.BranchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.entry.service.EntryService;
import plus.xyc.server.i18n.enums.I18nCode;

import java.util.List;
import java.util.Map;

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
    @Resource
    private ActivityService activityService;
    @Resource
    private EntryService entryService;

    private final static Map<String, Integer> BRANCH_TYPE = Map.of(
        "clone", 1,
        "empty", 0
    );

    @Override
    public void createDefault(Long moduleId) {
        Branch branch = new Branch();
        branch.setModuleId(moduleId);
        branch.setName("main");
        branch.setIsDefault(true);
        save(branch);
    }

    @Override
    public List<BranchResponse> all(BranchAllRequest request) {
        return branchMapStruct.allToBranchResponse(baseMapper.all(request));
    }

    @Override
    public PageResult<Branch> list(PageQueryRequest page, BranchListRequest request) {
        Page<Branch> p = page.toPage();
        List<Branch> all = baseMapper.list(p, request);
        return PageResult.of(p.getTotal(), all);
    }

    @Override
    @Transactional
    @DistributedLock(value = "branch:create", key = "#request.moduleId")
    public void create(BranchCreateRequest request) {
        int size = baseMapper.countByModuleIdAndName(request.getModuleId(), request.getName());
        if (size > 0) {
            throw new ResultException(I18nCode.BRANCH_EXISTED.code, MessageUtils.get(I18nCode.BRANCH_EXISTED.key));
        }
        Branch branch = new Branch();
        branch.setModuleId(request.getModuleId());
        branch.setName(request.getName());
        branch.setType(BRANCH_TYPE.get(request.getType()));
        save(branch);
        if(request.getType().equals("clone")) {
            Branch defaultBranch = getDefault(request.getModuleId());
            entryService.cloneEntriesBySourceId(defaultBranch.getId(), branch.getId());
        }

        activityService.branch(request.getModuleId(), ActivityOperate.ADD.code, branch);
    }

    @Override
    public Branch getDefault(Long moduleId) {
        return baseMapper.findDefaultByModuleId(moduleId);
    }

    @Override
    public List<Branch> getByNames(Long moduleId, List<String> names) {
        if(names.isEmpty()) {
            return List.of();
        }
        return baseMapper.findByNameIn(moduleId, names);
    }
}
