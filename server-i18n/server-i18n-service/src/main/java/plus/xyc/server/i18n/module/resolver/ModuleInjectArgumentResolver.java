package plus.xyc.server.i18n.module.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;
import plus.xyc.server.i18n.module.annotation.ModuleInject;
import plus.xyc.server.i18n.module.entity.dto.Module;
import plus.xyc.server.i18n.module.service.ModuleService;

import java.util.Map;

@Slf4j
public class ModuleInjectArgumentResolver implements HandlerMethodArgumentResolver {
    private final ModuleService moduleService;

    public ModuleInjectArgumentResolver(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    /**
     * 入参筛选
     *
     * @param methodParameter 参数集合
     * @return 格式化后的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(ModuleInject.class) && methodParameter.getParameterType().equals(Module.class);
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
        ModuleInject moduleInject = methodParameter.getParameterAnnotation(ModuleInject.class);
        log.info("ModuleInjectArgumentResolver moduleInject: {}", moduleInject);
        if(moduleInject == null) return null;
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null) return null;
        Map<String, String> path = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        log.info("ModuleInjectArgumentResolver uriTemplateVariables: {}", path);
        return moduleService.findByIdentifier(path.get("module"));
    }
}
