package plus.xyc.server.i18n.configurer;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import plus.xyc.server.i18n.module.resolver.ModuleInjectArgumentResolver;
import plus.xyc.server.i18n.module.service.ModuleService;

import java.util.List;

@Configuration
public class AppWebMvcConfigurer implements WebMvcConfigurer {

    @Lazy
    @Resource
    private ModuleService moduleService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //注入用户信息
        argumentResolvers.add(new ModuleInjectArgumentResolver(moduleService));
    }

}
