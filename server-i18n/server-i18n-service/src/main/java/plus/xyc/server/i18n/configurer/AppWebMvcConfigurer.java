package plus.xyc.server.i18n.configurer;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import plus.xyc.server.i18n.common.resolver.PathInjectArgumentResolver;
import plus.xyc.server.i18n.entry.service.EntryService;
import plus.xyc.server.i18n.file.service.FileService;
import plus.xyc.server.i18n.module.service.ModuleService;
import plus.xyc.server.i18n.open.resolver.OpenUserArgumentResolver;
import plus.xyc.server.main.api.service.MainAccountApiService;

import java.util.List;

@Configuration
public class AppWebMvcConfigurer implements WebMvcConfigurer {

    @Lazy
    @Resource
    private ModuleService moduleService;
    @Lazy
    @Resource
    private FileService fileService;
    @Lazy
    @Resource
    private EntryService entryService;
    @Lazy
    @DubboReference
    private MainAccountApiService mainAccountApiService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //注入用户信息
        argumentResolvers.add(new PathInjectArgumentResolver(moduleService, fileService, entryService));
        argumentResolvers.add(new OpenUserArgumentResolver(mainAccountApiService));
    }

}
