package plus.xyc.server.wiki.configurer;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import plus.xyc.server.wiki.book.resolver.BookInjectArgumentResolver;
import plus.xyc.server.wiki.book.service.BookService;

import java.util.List;

@Configuration
public class AppWebMvcConfigurer implements WebMvcConfigurer {

    @Lazy
    @Resource
    private BookService bookService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //注入用户信息
        argumentResolvers.add(new BookInjectArgumentResolver(bookService));
    }

}
