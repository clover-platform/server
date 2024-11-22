package plus.xyc.server.i18n.common.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;
import plus.xyc.server.i18n.branch.service.BranchService;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.module.service.ModuleService;

import java.util.Map;

@Slf4j
public class PathInjectArgumentResolver implements HandlerMethodArgumentResolver {
    private final ModuleService moduleService;
    private final BranchService branchService;

    public PathInjectArgumentResolver(ModuleService moduleService, BranchService branchService) {
        this.moduleService = moduleService;
        this.branchService = branchService;
    }

    /**
     * 入参筛选
     *
     * @param methodParameter 参数集合
     * @return 格式化后的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(PathInject.class) && methodParameter.getParameterType().equals(PathRequest.class);
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
        PathInject pathInject = methodParameter.getParameterAnnotation(PathInject.class);
        log.info("PathInjectArgumentResolver pathInject: {}", pathInject);
        if(pathInject == null) return null;
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null) return null;
        Map<String, String> path = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        log.info("PathInjectArgumentResolver uriTemplateVariables: {}", path);
        PathRequest pathRequest = new PathRequest();
        String moduleName = path.get("moduleName");
        String branchName = path.get("branchName");
        if(moduleName != null) {
            pathRequest.setModule(moduleService.findByIdentifier(moduleName));
        }
        if(branchName != null && !"-".equals(branchName)) {
            pathRequest.setBranch(branchService.findByName(pathRequest.getModule().getId(), branchName));
        }
        return pathRequest;
    }
}
