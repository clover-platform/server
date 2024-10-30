package plus.xyc.server.wiki.book.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;
import plus.xyc.server.wiki.book.annotation.BookInject;
import plus.xyc.server.wiki.book.entity.dto.Book;
import plus.xyc.server.wiki.book.service.BookService;

import java.util.Map;

@Slf4j
public class BookInjectArgumentResolver implements HandlerMethodArgumentResolver {
    private final BookService bookService;

    public BookInjectArgumentResolver(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * 入参筛选
     *
     * @param methodParameter 参数集合
     * @return 格式化后的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(BookInject.class) && methodParameter.getParameterType().equals(Book.class);
    }

    /**
     * @param methodParameter       入参集合
     * @param modelAndViewContainer model 和 view
     * @param nativeWebRequest      web相关
     * @param webDataBinderFactory  入参解析
     * @return 包装对象
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object resolveArgument(
            MethodParameter methodParameter,
            ModelAndViewContainer modelAndViewContainer,
            @NonNull NativeWebRequest nativeWebRequest,
            WebDataBinderFactory webDataBinderFactory
    ) {
        BookInject bookInject = methodParameter.getParameterAnnotation(BookInject.class);
        log.info("BookInjectArgumentResolver bookInject: {}", bookInject);
        if(bookInject == null) return null;
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null) return null;
        Map<String, String> path = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        log.info("BookInjectArgumentResolver uriTemplateVariables: {}", path);
        return bookService.findByPath(path.get("bookPath"));
    }
}
