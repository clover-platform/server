package plus.xyc.server.i18n.module.service.impl;

import com.github.pagehelper.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.zkit.support.server.ai.api.service.VectorStoreApiService;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;

import plus.xyc.server.i18n.common.enums.I18nCode;
import plus.xyc.server.i18n.file.mapper.FileMapper;
import plus.xyc.server.i18n.file.service.FileService;
import plus.xyc.server.i18n.member.entity.enums.MemberRoleType;
import plus.xyc.server.i18n.member.entity.response.MemberResponse;
import plus.xyc.server.i18n.member.service.MemberService;
import plus.xyc.server.i18n.module.entity.dto.Module;
import plus.xyc.server.i18n.module.entity.dto.ModuleCollect;
import plus.xyc.server.i18n.module.entity.dto.ModuleTargetLanguage;
import plus.xyc.server.i18n.module.entity.mapstruct.ModuleMapStruct;
import plus.xyc.server.i18n.module.entity.request.ModuleAllRequest;
import plus.xyc.server.i18n.module.entity.request.ModuleCreateRequest;
import plus.xyc.server.i18n.module.entity.request.ModuleQueryRequest;
import plus.xyc.server.i18n.module.entity.request.ModuleUpdateRequest;
import plus.xyc.server.i18n.module.entity.response.*;
import plus.xyc.server.i18n.module.mapper.ModuleMapper;
import plus.xyc.server.i18n.module.service.ModuleCollectService;
import plus.xyc.server.i18n.module.service.ModuleCountService;
import plus.xyc.server.i18n.module.service.ModuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.module.service.ModuleTargetLanguageService;
import plus.xyc.server.main.api.entity.request.ApiAccountListRequest;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.service.MainAccountApiService;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private FileService fileService;
    @DubboReference
    private MainAccountApiService mainAccountApiService;
    @Resource
    private FileMapper fileMapper;
    @Resource
    private ModuleCountService moduleCountService;
    @Resource
    private ModuleCollectService moduleCollectService;
    @DubboReference
    private VectorStoreApiService vectorStoreApiService;

    @Override
    public PageResult<ModuleResponse> query(PageRequest pageRequest, ModuleQueryRequest query) {
        ApiAccountResponse account = mainAccountApiService.getById(query.getUserId());
        if(account == null)
            throw new ResultException(I18nCode.USER_NOT_FOUND.code, MessageUtils.get(I18nCode.USER_NOT_FOUND.key));
        query.setProjectId(account.getCurrentProjectId());
        try(Page<Module> page = pageRequest.start()) {
            baseMapper.query(pageRequest.getKeyword(), query);
            return PageResult.of(page.getTotal(), wrapResponse(query.getUserId(), page.getResult()));
        }
    }

    public List<ModuleResponse> wrapResponse(Long userId, List<Module> modules) {
        List<Long> moduleIds = modules.stream().map(Module::getId).toList();
        List<SizeResponse> memberSizes = memberSizes(moduleIds);
        List<SizeResponse> targetSizes = targetSizes(moduleIds);
        List<ModuleCountResponse> counts = moduleCountService.getCounts(moduleIds);
        List<ModuleCollect> collects = moduleCollectService.findByUserIdAndModuleIds(userId, moduleIds);
        List<Long> collectIds = collects.stream().map(ModuleCollect::getModuleId).toList();
        return modules.stream().map(module -> {
            ModuleResponse response = moduleMapStruct.toModuleResponse(module);
            response.setWordSize(counts.stream().filter(count -> count.getModuleId().equals(module.getId())).findFirst().map(ModuleCountResponse::getWordCount).orElse(0L));
            response.setMemberSize(memberSizes.stream().filter(size -> size.getId().equals(module.getId())).findFirst().map(SizeResponse::getSize).orElse(0));
            response.setTargetSize(targetSizes.stream().filter(size -> size.getId().equals(module.getId())).findFirst().map(SizeResponse::getSize).orElse(0));
            response.setCollected(collectIds.contains(module.getId()));
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
        PageResult<ApiAccountResponse> adminUsersResult = mainAccountApiService.list(apiRequest);

        List<SizeResponse> targetSizes = targetSizes(List.of(module.getId()));
        List<ModuleLanguageResponse> languages = languages(id);

        ModuleDashboardResponse response = new ModuleDashboardResponse();
        response.setDetail(moduleMapStruct.toModuleResponse(module));
        response.setMembers(members);
        response.setLanguages(languages);
        ModuleCountResponse countResponse = new ModuleCountResponse();
        countResponse.setTargetCount(targetSizes.stream().findFirst().map(SizeResponse::getSize).orElse(0));
        List<ModuleCountResponse> counts = moduleCountService.getCounts(List.of(id));
        countResponse.setWordCount(counts.stream().findFirst().map(ModuleCountResponse::getWordCount).orElse(0L));
        // countResponse.setBranchCount(branchMapper.countByModuleId(id));
        // countResponse.setBranchCount(branchMapper.countByModuleId(id));
        countResponse.setMemberCount(members.size());
        response.setCount(countResponse);
        response.setMembers(admins.stream().map(admin -> {
            ApiAccountResponse user = adminUsersResult.getData().stream().filter(u -> u.getId().equals(admin.getAccountId())).findFirst().orElse(null);
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
    @CacheEvict(value = "i18n:module", key = "#result.identifier")
    public Module delete(Long id, Long userId) {
        Module module = getById(id);
        lambdaUpdate().set(Module::getDeleted, true).eq(Module::getId, id).update();

        Map<String, String> metadata = new HashMap<>();
        metadata.put("source", "i18n");
        metadata.put("moduleId", id.toString());
        vectorStoreApiService.delete(metadata);

        return module;
    }

    @Override
    public ModuleResponse detail(Long id) {
        Module module = getById(id);
        ModuleResponse response = moduleMapStruct.toModuleResponse(module);
        response.setLanguages(languages(id));
        return response;
    }

    @Override
    public void update(ModuleUpdateRequest request) {
        lambdaUpdate().set(Module::getName, request.getName())
                .set(Module::getDescription, request.getDescription())
                .set(Module::getUpdateTime, new Date())
                .set(Module::getUpdateUser, request.getUserId())
                .eq(Module::getId, request.getId())
                .update();
    }

    @Override
    @Cacheable(value = "i18n:module", key = "#identifier", unless = "#result == null")
    public Module findByIdentifier(String identifier) {
        return baseMapper.findOneByIdentifier(identifier);
    }

    @Override
    @Transactional
    public void leave(Long userId, Long projectId) {
        // 删除我创建的 ?
        // 从接入的模块中删除我
        List<Module> modules = lambdaQuery().eq(Module::getProjectId, projectId).list();
        List<Long> moduleIds = modules.stream().map(Module::getId).toList();
        memberService.delete(userId, moduleIds);
    }
}
