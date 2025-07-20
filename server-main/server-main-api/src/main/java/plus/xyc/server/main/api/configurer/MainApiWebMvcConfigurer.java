package plus.xyc.server.main.api.configurer;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.zkit.support.starter.security.service.SessionService;

import plus.xyc.server.main.api.resolver.CurrentAccountResolver;
import plus.xyc.server.main.api.service.MainAccountApiService;

import java.util.List;

@Configuration
public class MainApiWebMvcConfigurer implements WebMvcConfigurer {

    @Lazy
    @DubboReference
    private MainAccountApiService mainAccountApiService;
    @Lazy
    @Resource
    private SessionService sessionService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CurrentAccountResolver(sessionService, mainAccountApiService));
    }

}
