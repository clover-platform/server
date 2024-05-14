package plus.xyc.server.i18n.module.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.activity.entity.enums.ActivityOperate;
import plus.xyc.server.i18n.activity.service.ActivityService;
import plus.xyc.server.i18n.branch.service.BranchService;
import plus.xyc.server.i18n.enums.I18nCode;
import plus.xyc.server.i18n.language.entity.response.LanguageResponse;
import plus.xyc.server.i18n.language.entity.response.LanguageWithCountResponse;
import plus.xyc.server.i18n.member.entity.enums.MemberRoleType;
import plus.xyc.server.i18n.member.entity.response.MemberResponse;
import plus.xyc.server.i18n.member.service.MemberService;
import plus.xyc.server.i18n.module.entity.dto.Module;
import plus.xyc.server.i18n.module.entity.dto.ModuleTargetLanguage;
import plus.xyc.server.i18n.module.entity.mapstruct.ModuleMapStruct;
import plus.xyc.server.i18n.module.entity.request.CreateModuleRequest;
import plus.xyc.server.i18n.module.entity.request.ModuleQueryRequest;
import plus.xyc.server.i18n.module.entity.response.ModuleDashboardResponse;
import plus.xyc.server.i18n.module.entity.response.ModuleResponse;
import plus.xyc.server.i18n.module.entity.response.SizeResponse;
import plus.xyc.server.i18n.module.mapper.ModuleMapper;
import plus.xyc.server.i18n.module.service.ModuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.module.service.ModuleTargetLanguageService;
import plus.xyc.server.main.api.entity.request.AccountRequest;
import plus.xyc.server.main.api.entity.response.AccountResponse;
import plus.xyc.server.main.api.rest.MainAccountRestApi;

import java.util.Arrays;
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
    @DistributedLock(value = "i18n:module:create")
    @Transactional
    public void create(CreateModuleRequest request) {
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

        AccountRequest request = new AccountRequest();
        request.setIds(adminIds);
        request.setSize(adminIds.size());
        Result<List<AccountResponse>> adminUsersResult = mainAccountRestApi.list(request);

        List<SizeResponse> targetSizes = targetSizes(List.of(module.getId()));
        List<LanguageResponse> languages = languages(id);
        /**
         * const languagesWithCount: LanguageWithCount[] = [];
         *     for(const language of languages) {
         *       const count = await this.entryService.count({
         *         moduleId: module.id,
         *         language: language.code,
         *       });
         *       languagesWithCount.push({
         *         ...language,
         *         ...count,
         *       })
         *     }
         */

        ModuleDashboardResponse response = new ModuleDashboardResponse();
        response.setDetail(moduleMapStruct.toModuleResponse(module));
        return response;
    }

    public List<LanguageResponse> languages(Long id) {
        return moduleTargetLanguageService.languages(id);
    }
}
