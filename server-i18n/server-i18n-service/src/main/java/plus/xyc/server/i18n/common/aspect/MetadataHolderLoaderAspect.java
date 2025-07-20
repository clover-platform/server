package plus.xyc.server.i18n.common.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.zkit.support.server.message.api.holder.MetadataHolder;

import com.alibaba.fastjson2.JSONObject;

import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.i18n.common.entity.PathRequest;
import plus.xyc.server.i18n.module.entity.dto.Module;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

@Aspect
@Component
@Slf4j
@Order(10)
public class MetadataHolderLoaderAspect {

    @Pointcut("@annotation(plus.xyc.server.i18n.common.annotation.MetadataHolderLoader)")
    public void metadataHolderLoaderPointcut() {
    }

    @Around(value = "metadataHolderLoaderPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        JSONObject metadata = new JSONObject();
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> {
            if (arg instanceof ApiAccountResponse account) {
                metadata.put("projectId", account.getCurrentProjectId());
                log.info("MetadataHolderLoaderAspect projectId: {}", account.getCurrentProjectId());
            }
            if (arg instanceof PathRequest pathRequest) {
                Module module = pathRequest.getModule();
                if (module != null) {
                    metadata.put("moduleId", module.getId());
                    log.info("MetadataHolderLoaderAspect moduleId: {}", module.getId());
                }
            }
        });
        
        MetadataHolder.set(metadata);
        return joinPoint.proceed();
    }

    @AfterReturning("metadataHolderLoaderPointcut()")
    public void afterReturning(JoinPoint joinPoint) {
        log.info("MetadataHolderLoaderAspect afterReturning MetadataHolder.clear()");
        MetadataHolder.clear();
    }
}
