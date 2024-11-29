package plus.xyc.server.i18n.configurer;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import plus.xyc.server.i18n.branch.service.BranchService;
import plus.xyc.server.i18n.common.resolver.PathInjectArgumentResolver;
import plus.xyc.server.i18n.entry.service.EntryService;
import plus.xyc.server.i18n.module.service.ModuleService;
import plus.xyc.server.i18n.open.resolver.OpenUserArgumentResolver;
import plus.xyc.server.main.api.rest.MainAccountRestApi;

import java.util.List;

@Configuration
public class AppWebMvcConfigurer implements WebMvcConfigurer {

    @Lazy
    @Resource
    private ModuleService moduleService;
    @Lazy
    @Resource
    private BranchService branchService;
    @Lazy
    @Resource
    private EntryService entryService;
    @Lazy
    @Resource
    private MainAccountRestApi mainAccountRestApi;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //注入用户信息
        argumentResolvers.add(new PathInjectArgumentResolver(moduleService, branchService, entryService));
        argumentResolvers.add(new OpenUserArgumentResolver(mainAccountRestApi));
    }

}
