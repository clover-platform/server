package plus.xyc.server.i18n.branch.service.impl;

import com.github.pagehelper.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.AopUtils;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.activity.entity.enums.ActivityOperate;
import plus.xyc.server.i18n.activity.service.ActivityService;
import plus.xyc.server.i18n.branch.entity.dto.Branch;
import plus.xyc.server.i18n.branch.entity.mapstruct.BranchMapStruct;
import plus.xyc.server.i18n.branch.entity.request.*;
import plus.xyc.server.i18n.branch.entity.response.BranchMergeEntriesResponse;
import plus.xyc.server.i18n.branch.entity.response.BranchMergeOverviewResponse;
import plus.xyc.server.i18n.branch.entity.response.BranchResponse;
import plus.xyc.server.i18n.branch.mapper.BranchMapper;
import plus.xyc.server.i18n.branch.service.BranchRevisionService;
import plus.xyc.server.i18n.branch.service.BranchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.response.EntryWithResultResponse;
import plus.xyc.server.i18n.entry.service.EntryService;
import plus.xyc.server.i18n.common.enums.I18nCode;
import plus.xyc.server.i18n.open.entity.request.OpenBranchCreateRequest;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    @Resource
    private BranchRevisionService branchRevisionService;

    private final static Map<String, Integer> BRANCH_TYPE = Map.of(
        "clone", 1,
        "empty", 0
    );

    @Override
    public List<BranchResponse> all(BranchAllRequest request) {
        return branchMapStruct.allToBranchResponse(baseMapper.all(request));
    }

    @Override
    public PageResult<Branch> list(PageRequest pr, BranchListRequest request) {
        try(Page<Branch> page = pr.start()) {
            baseMapper.list(request);
            return PageResult.of(page);
        }
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:branch:create:'+#moduleId")
    public void createDefault(Long moduleId, Long userId) {
        Branch branch = new Branch();
        branch.setModuleId(moduleId);
        branch.setName("main");
        branch.setIsDefault(true);
        save(branch);
        branchRevisionService.init(branch.getId(), userId);
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:branch:create:'+#request.moduleId")
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

        branchRevisionService.init(branch.getId(), request.getUserId());

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

    @Override
    @Transactional
    @DistributedLock("'i18n:branch:update:'+#request.id")
    public void rename(BranchRenameRequest request) {
        Branch origin = baseMapper.findOneByModuleIdAndName(request.getModuleId(), request.getName());
        if(origin != null && !origin.getId().equals(request.getId())) {
            throw new ResultException(I18nCode.BRANCH_EXISTED.code, MessageUtils.get(I18nCode.BRANCH_EXISTED.key));
        }
        this.update().set("name", request.getName()).eq("id", request.getId()).update();
        activityService.branch(request.getModuleId(), ActivityOperate.UPDATE.code, request);
    }

    @Override
    @Transactional
    @DistributedLock("'i18n:branch:update'+#id")
    public void delete(Long id) {
        Branch branch = getById(id);
        this.update().set("deleted", true).eq("id", id).update();
        activityService.branch(branch.getModuleId(), ActivityOperate.DELETE.code, branch);
    }

    @Override
    public BranchMergeOverviewResponse mergeOverview(Long id) {
        BranchMergeEntriesResponse entriesResponse = getMergeEntries(id);
        BranchMergeOverviewResponse response = new BranchMergeOverviewResponse();
        response.setDeleted(entriesResponse.getDeleted().size());
        response.setAdded(entriesResponse.getAdded().size());
        response.setChanged(entriesResponse.getChanged().size());
        return response;
    }

    @Override
    public BranchMergeEntriesResponse getMergeEntries(Long id) {
        Branch branch = getById(id);
        Branch defaultBranch = getDefault(branch.getModuleId());
        List<Entry> entries = entryService.getByBranchId(id);
        List<Entry> defaultEntries = entryService.getByBranchId(defaultBranch.getId());
        // branch.type 为 0 的时候，不删除，只添加和修改
        List<Entry> deleted = defaultEntries.stream().filter(entry -> entries.stream().noneMatch(e -> e.getIdentifier().equals(entry.getIdentifier()))).toList();
        List<Entry> added = entries.stream().filter(entry -> defaultEntries.stream().noneMatch(e -> e.getIdentifier().equals(entry.getIdentifier()))).toList();
        List<Entry> changed = entries.stream().filter(entry -> {
            Entry defaultEntry = defaultEntries.stream().filter(e -> e.getIdentifier().equals(entry.getIdentifier())).findFirst().orElse(null);
            return defaultEntry != null && !defaultEntry.getValue().equals(entry.getValue());
        }).toList();
        // entries 里面 所有 与 defaultEntries 相同的 key 的 entry
        List<Entry> merged = entries.stream().filter(entry -> defaultEntries.stream().anyMatch(e -> e.getIdentifier().equals(entry.getIdentifier()))).toList();
        BranchMergeEntriesResponse response = new BranchMergeEntriesResponse();
        response.setAdded(added);
        response.setChanged(changed);
        response.setDeleted(Objects.equals(branch.getType(), BRANCH_TYPE.get("clone")) ? deleted : List.of());
        response.setMerged(merged);
        response.setBranch(branch);
        response.setDefaultBranch(defaultBranch);
        return response;
    }

    @Override
    @Transactional
    @DistributedLock({"'i18n:branch:merge:'+#request.id", "'i18n:entry:create:'+#request.moduleId"})
    public void merge(BranchMergeRequest request) {
        log.info("merge request: {}", request);
        log.info("start");
        List<EntryWithResultResponse> sources = entryService.getEntryByBranchIdWithResult(request.getId());
        BranchMergeEntriesResponse entries = getMergeEntries(request.getId());
        List<Entry> deleted = entries.getDeleted();
        List<Entry> added = entries.getAdded();
        List<Entry> merged = entries.getMerged();
        Branch defaultBranch = entries.getDefaultBranch();

        // 需要删除的
        log.info("deleted");
        List<Long> deleteEntryIds = deleted.stream().map(Entry::getId).toList();
        if(!deleteEntryIds.isEmpty()){
            entryService.update().set("deleted", true).in("id", deleteEntryIds).update();
        }

        // 需要添加的
        log.info("added");
        if(!added.isEmpty()){
            List<EntryWithResultResponse> newEntries = sources.stream().filter(entry -> added.stream().anyMatch(e -> e.getIdentifier().equals(entry.getIdentifier()))).toList();
            entryService.cloneEntries(newEntries, defaultBranch.getId());
        }

        // 需要更新的
        // 先删除旧的
        log.info("merged");
        List<String> updateEntryKeys = merged.stream().map(Entry::getIdentifier).toList();
        if(!updateEntryKeys.isEmpty()){
            entryService.update().set("deleted", true).eq("branch_id", defaultBranch.getId()).in("identifier", updateEntryKeys).update();
        }
        // 新的添加到默认分支
        if(!merged.isEmpty()){
            List<EntryWithResultResponse> newEntries = sources.stream().filter(entry -> updateEntryKeys.contains(entry.getIdentifier())).toList();
            entryService.cloneEntries(newEntries, defaultBranch.getId());
        }

        if(request.getDeleteAfterMerge()){
            update().set("deleted", true).eq("id", request.getId()).update();
        }
        log.info("end");
    }

    @Override
    @Cacheable(value = "i18n:branch", key = "#moduleId + ':' + #name")
    public Branch findByName(Long moduleId, String name) {
        return baseMapper.findOneByModuleIdAndNameAndDeleted(moduleId, name, false);
    }

    @Override
    public void createIfNotExist(OpenBranchCreateRequest request) {
        log.info("createIfNotExist: {}", request);
        Branch branch = baseMapper.findOneByModuleIdAndNameAndDeleted(request.getModuleId(), request.getName(), false);
        if(branch == null) {
            BranchCreateRequest createRequest = new BranchCreateRequest();
            createRequest.setModuleId(request.getModuleId());
            createRequest.setName(request.getName());
            createRequest.setType(request.getClone() ? "clone" : "empty");
            createRequest.setUserId(request.getUserId());
            BranchService self = AopUtils.current(BranchService.class);
            self.create(createRequest);
        }
    }
}
