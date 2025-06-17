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
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.boot.utils.MessageUtils;
import plus.xyc.server.i18n.common.annotation.PathInject;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.common.enums.I18nCode;
import plus.xyc.server.i18n.entry.service.EntryService;
import plus.xyc.server.i18n.file.service.FileService;
import plus.xyc.server.i18n.module.entity.dto.Module;
import plus.xyc.server.i18n.module.service.ModuleService;

import java.util.Map;

@Slf4j
public class PathInjectArgumentResolver implements HandlerMethodArgumentResolver {
    private final ModuleService moduleService;
    private final FileService fileService;
    private final EntryService entryService;

    public PathInjectArgumentResolver(
            ModuleService moduleService,
            FileService fileService,
            EntryService entryService
    ) {
        this.moduleService = moduleService;
        this.fileService = fileService;
        this.entryService = entryService;
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
        String fileName = path.get("fileName");
        String entryId = path.get("entryId");
        if(moduleName != null) {
            Module module = moduleService.findByIdentifier(moduleName);
            if(module == null)
                throw new ResultException(I18nCode.MODULE_NOT_FOUND.code, MessageUtils.get(I18nCode.MODULE_NOT_FOUND.key));
            pathRequest.setModule(moduleService.findByIdentifier(moduleName));
        }
        if(fileName != null && !"-".equals(fileName)) {
            pathRequest.setFile(fileService.findByName(pathRequest.getModule().getId(), fileName));
        }
        if(entryId != null) {
            pathRequest.setEntry(entryService.findById(Long.parseLong(entryId)));
        }
        return pathRequest;
    }
}
