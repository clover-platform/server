package plus.xyc.server.i18n.open.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.RequestUtils;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.i18n.open.annotation.OpenUser;
import plus.xyc.server.main.api.service.MainAccountApiService;

@Slf4j
public class OpenUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final MainAccountApiService mainAccountRestApi;

    public OpenUserArgumentResolver(MainAccountApiService mainAccountRestApi) {
        this.mainAccountRestApi = mainAccountRestApi;
    }

    /**
     * 入参筛选
     *
     * @param methodParameter 参数集合
     * @return 格式化后的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(OpenUser.class) && methodParameter.getParameterType().equals(SessionUser.class);
    }

    /**
     * @param methodParameter       入参集合
     * @param modelAndViewContainer model 和 view
     * @param nativeWebRequest      web相关
     * @param webDataBinderFactory  入参解析
     * @return 包装对象
     */
    @Override
    public Object resolveArgument(
            MethodParameter methodParameter,
            ModelAndViewContainer modelAndViewContainer,
            @NonNull NativeWebRequest nativeWebRequest,
            WebDataBinderFactory webDataBinderFactory
    ) {
        OpenUser openUser = methodParameter.getParameterAnnotation(OpenUser.class);
        if(openUser == null) return null;
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null) return null;
        String token = RequestUtils.getToken(request);
        if(token == null || token.isEmpty())
            throw ResultException.unauthorized();
        return mainAccountRestApi.checkAccessToken(token);
    }
}
