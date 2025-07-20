package plus.xyc.server.main.api.resolver;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.zkit.support.starter.boot.utils.RequestUtils;
import org.zkit.support.starter.security.entity.SessionUser;
import org.zkit.support.starter.security.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;
import plus.xyc.server.main.api.annotation.CurrentAccount;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.service.MainAccountApiService;

@Slf4j
public class CurrentAccountResolver implements HandlerMethodArgumentResolver {

    private final MainAccountApiService mainAccountApiService;
    private final SessionService sessionService;

    public CurrentAccountResolver(SessionService sessionService, MainAccountApiService mainAccountApiService) {
        this.mainAccountApiService = mainAccountApiService;
        this.sessionService = sessionService;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(CurrentAccount.class) && methodParameter.getParameterType().equals(ApiAccountResponse.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter methodParameter,
            ModelAndViewContainer modelAndViewContainer,
            @NonNull NativeWebRequest nativeWebRequest,
            WebDataBinderFactory webDataBinderFactory) {
        CurrentAccount currentAccount = methodParameter.getParameterAnnotation(CurrentAccount.class);
        if (currentAccount == null)
            return null;
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null)
            return null;
        String token = RequestUtils.getToken(request);
        if (token == null)
            return null;
        SessionUser sessionUser = sessionService.get(token);
        if (sessionUser == null)
            return null;
        ApiAccountResponse account = mainAccountApiService.getById(sessionUser.getId());
        return account;
    }
    
}
