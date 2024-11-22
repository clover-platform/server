package plus.xyc.server.i18n.module.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.activity.entity.enums.ActivityOperate;
import plus.xyc.server.i18n.activity.service.ActivityService;
import plus.xyc.server.i18n.branch.mapper.BranchMapper;
import plus.xyc.server.i18n.branch.service.BranchService;
import plus.xyc.server.i18n.entry.service.EntryService;
import plus.xyc.server.i18n.common.enums.I18nCode;
import plus.xyc.server.i18n.member.entity.enums.MemberRoleType;
import plus.xyc.server.i18n.member.entity.response.MemberResponse;
import plus.xyc.server.i18n.member.service.MemberService;
import plus.xyc.server.i18n.module.entity.dto.Module;
import plus.xyc.server.i18n.module.entity.dto.ModuleTargetLanguage;
import plus.xyc.server.i18n.module.entity.mapstruct.ModuleMapStruct;
import plus.xyc.server.i18n.module.entity.request.ModuleAllRequest;
import plus.xyc.server.i18n.module.entity.request.ModuleCreateRequest;
import plus.xyc.server.i18n.module.entity.request.ModuleQueryRequest;
import plus.xyc.server.i18n.module.entity.request.ModuleUpdateRequest;
import plus.xyc.server.i18n.module.entity.response.*;
import plus.xyc.server.i18n.module.mapper.ModuleMapper;
import plus.xyc.server.i18n.module.service.ModuleAccessService;
import plus.xyc.server.i18n.module.service.ModuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.module.service.ModuleTargetLanguageService;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.rest.MainAccountRestApi;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 项目 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
@Slf4j
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, Module> implements ModuleService {

    @Resource
    private ModuleMapStruct moduleMapStruct;
    @Resource
    private ModuleTargetLanguageService moduleTargetLanguageService;
    @Resource
    private MemberService memberService;
    @Resource
    private BranchService branchService;
    @Resource
    private ActivityService activityService;
    @Resource
    private MainAccountRestApi mainAccountRestApi;
    @Resource
    private EntryService entryService;
    @Resource
    private BranchMapper branchMapper;
    @Resource
    private ModuleAccessService moduleAccessService;

    @Override
    public PageResult<ModuleResponse> query(PageQueryRequest pageRequest, ModuleQueryRequest query) {
        Page<Module> page = pageRequest.toPage();
        List<Module> modules = baseMapper.query(page, pageRequest.getKeyword(), query);
        return PageResult.of(page.getTotal(), wrapResponse(modules));
    }

    public List<ModuleResponse> wrapResponse(List<Module> modules) {
        List<Long> moduleIds = modules.stream().map(Module::getId).toList();
        List<SizeResponse> memberSizes = memberSizes(moduleIds);
        List<SizeResponse> targetSizes = targetSizes(moduleIds);
        return modules.stream().map(module -> {
            ModuleResponse response = moduleMapStruct.toModuleResponse(module);
            response.setWordSize(entryService.wordCount(module.getId()));
            response.setMemberSize(memberSizes.stream().filter(size -> size.getId().equals(module.getId())).findFirst().map(SizeResponse::getSize).orElse(0));
            response.setTargetSize(targetSizes.stream().filter(size -> size.getId().equals(module.getId())).findFirst().map(SizeResponse::getSize).orElse(0));
            return response;
        }).toList();
    }

    public List<SizeResponse> memberSizes(List<Long> moduleIds) {
        if(moduleIds.isEmpty())
            return List.of();
        return baseMapper.memberSizes(moduleIds);
    }

    public List<SizeResponse> targetSizes(List<Long> moduleIds) {
        if(moduleIds.isEmpty())
            return List.of();
        return baseMapper.targetSizes(moduleIds);
    }

    @Override
    @DistributedLock(value = "i18n:module:create", el = false)
    @Transactional
    public void create(ModuleCreateRequest request) {
        int size = baseMapper.countByIdentifier(request.getIdentifier());
        if(size > 0)
            throw new ResultException(I18nCode.MODULE_IDENTIFIER_EXIST.code, MessageUtils.get(I18nCode.MODULE_IDENTIFIER_EXIST.key));
        // 保存项目信息
        Module module = new Module();
        module.setIdentifier(request.getIdentifier());
        module.setName(request.getName());
        module.setOwner(request.getOwner());
        module.setDescription(request.getDescription());
        module.setSource(request.getSource());
        module.setProjectId(request.getProjectId());
        save(module);

        // 保存目标语言
        List<ModuleTargetLanguage> targets = Arrays.stream(request.getTargets()).map(target -> {
            ModuleTargetLanguage targetLanguage = new ModuleTargetLanguage();
            targetLanguage.setModuleId(module.getId());
            targetLanguage.setCode(target);
            return targetLanguage;
        }).toList();
        moduleTargetLanguageService.saveBatch(targets);

        // 添加项目成员
        memberService.addModuleOwner(module.getId(), module.getOwner());

        // 创建默认分支
        branchService.createDefault(module.getId());

        // 记录日志
        activityService.module(module.getId(), ActivityOperate.ADD.code, module);
    }

    @Override
    public ModuleDashboardResponse dashboard(Long id) {
        Module module = getById(id);
        List<MemberResponse> members = memberService.findMembers(id);
        List<Integer> adminRoles = List.of(MemberRoleType.ADMIN.code, MemberRoleType.OWNER.code);
        List<MemberResponse> admins = members.stream().filter(member -> adminRoles.stream().anyMatch(role -> member.getRoles().contains(role))).toList();
        List<Long> adminIds = admins.stream().map(MemberResponse::getAccountId).toList();

        ApiAccountListRequest apiRequest = new ApiAccountListRequest();
        apiRequest.setIds(adminIds);
        apiRequest.setSize(adminIds.size());
        apiRequest.setPage(1);
        Result<PageResult<ApiAccountResponse>> adminUsersResult = mainAccountRestApi.list(apiRequest);
        if(!adminUsersResult.isSuccess()) {
            throw ResultException.internal();
        }

        List<SizeResponse> targetSizes = targetSizes(List.of(module.getId()));
        List<ModuleLanguageResponse> languages = languages(id);

        ModuleDashboardResponse response = new ModuleDashboardResponse();
        response.setDetail(moduleMapStruct.toModuleResponse(module));
        response.setMembers(members);
        response.setLanguages(languages);
        ModuleCountResponse countResponse = new ModuleCountResponse();
        countResponse.setTargetCount(targetSizes.stream().findFirst().map(SizeResponse::getSize).orElse(0));
        countResponse.setWordCount(entryService.wordCount(id));
        countResponse.setBranchCount(branchMapper.countByModuleId(id));
        countResponse.setMemberCount(members.size());
        response.setCount(countResponse);
        response.setMembers(admins.stream().map(admin -> {
            ApiAccountResponse user = adminUsersResult.getData().getData().stream().filter(u -> u.getId().equals(admin.getAccountId())).findFirst().orElse(null);
            MemberResponse member = new MemberResponse();
            member.setUser(user);
            member.setId(admin.getId());
            member.setAccountId(admin.getAccountId());
            member.setRoles(admin.getRoles());
            return member;
        }).toList());
        return response;
    }

    public List<ModuleLanguageResponse> languages(Long id) {
        return moduleTargetLanguageService.languages(id);
    }

    @Override
    public List<ModuleResponse> all(ModuleAllRequest request) {
        return baseMapper.all(request);
    }

    @Override
    public void delete(Long id, Long userId) {
        boolean checked = moduleAccessService.check(id, userId, List.of(MemberRoleType.OWNER.code));
        if(!checked) {
            throw new ResultException(I18nCode.ACCESS_ERROR.code, MessageUtils.get(I18nCode.ACCESS_ERROR.key));
        }
        update().set("deleted", true).eq("id", id).update();

        activityService.module(id, ActivityOperate.DELETE.code, id);
    }

    @Override
    public ModuleResponse detail(Long id) {
        Module module = getById(id);
        return moduleMapStruct.toModuleResponse(module);
    }

    @Override
    public void update(ModuleUpdateRequest request) {
        boolean checked = moduleAccessService.check(request.getId(), request.getUserId(), List.of(MemberRoleType.OWNER.code, MemberRoleType.ADMIN.code));
        if(!checked) {
            throw new ResultException(I18nCode.ACCESS_ERROR.code, MessageUtils.get(I18nCode.ACCESS_ERROR.key));
        }
        update()
                .set("name", request.getName())
                .set("description", request.getDescription())
                .set("update_time", new Date())
                .set("update_user", request.getUserId())
                .eq("id", request.getId())
                .update();
        activityService.module(request.getId(), ActivityOperate.UPDATE.code, getById(request.getId()));
    }

    @Override
    @Cacheable(value = "i18n:module", key = "#identifier")
    public Module findByIdentifier(String identifier) {
        return baseMapper.findOneByIdentifier(identifier);
    }
}
