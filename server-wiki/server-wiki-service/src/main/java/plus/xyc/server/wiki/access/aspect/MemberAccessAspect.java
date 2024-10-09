package plus.xyc.server.wiki.access.aspect;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.zkit.support.starter.boot.utils.RequestUtils;
import org.zkit.support.starter.security.entity.SessionUser;
import org.zkit.support.starter.security.service.SessionService;

import java.util.Map;

@Aspect
@Component
@Slf4j
public class MemberAccessAspect {

    @Resource
    private SessionService sessionService;

    @Pointcut("@annotation(plus.xyc.server.wiki.access.annotation.MemberAccess)")
    public void accessPointcut() {}

    @Before(value = "accessPointcut()")
    @SuppressWarnings("unchecked")
    public void doBefore(JoinPoint joinPoint) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        log.info("MemberAccessAspect URI: {}", request.getRequestURI());
        log.info("MemberAccessAspect args: {}", joinPoint.getArgs());
        SessionUser user = sessionService.get(RequestUtils.getToken(request));
        log.info("MemberAccessAspect user: {}", user);
        Map<String, String> path = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        log.info("MemberAccessAspect uriTemplateVariables: {}", path);
    }

}
