package plus.xyc.server.i18n.common.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import plus.xyc.server.i18n.common.annotation.Recount;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RecountAspect {

    @Pointcut("@annotation(plus.xyc.server.i18n.common.annotation.Recount)")
    public void pointcut() {}

    @After(value = "pointcut()")
    public void doAfter(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Recount recount = AnnotationUtils.getAnnotation(method, Recount.class);
        assert recount != null;
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        log.info("RecountAspect URI: {}", request.getRequestURI());
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> {
            log.info("RecountAspect arg: {}", arg);
        });
    }

}
