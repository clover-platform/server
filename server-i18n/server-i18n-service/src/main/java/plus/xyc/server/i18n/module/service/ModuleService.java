package plus.xyc.server.i18n.module.service;

import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.language.entity.response.LanguageResponse;
import plus.xyc.server.i18n.module.entity.dto.Module;
import com.baomidou.mybatisplus.extension.service.IService;
import plus.xyc.server.i18n.module.entity.request.CreateModuleRequest;
import plus.xyc.server.i18n.module.entity.request.ModuleQueryRequest;
import plus.xyc.server.i18n.module.entity.response.ModuleDashboardResponse;
import plus.xyc.server.i18n.module.entity.response.ModuleLanguageResponse;
import plus.xyc.server.i18n.module.entity.response.ModuleResponse;

import java.util.List;

/**
 * <p>
 * 项目 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface ModuleService extends IService<Module> {

    PageResult<ModuleResponse> query(PageQueryRequest page, ModuleQueryRequest query);
    void create(CreateModuleRequest request);
    ModuleDashboardResponse dashboard(Long id);
    List<ModuleLanguageResponse>  languages(Long id);

}
