package plus.xyc.server.main.api.aspect;

import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.zkit.support.starter.boot.utils.SpelExpressionUtils;
import org.zkit.support.starter.security.entity.SessionUser;

import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.main.api.aspect.annotation.AccountHolderLoader;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.holder.AccountHolder;
import plus.xyc.server.main.api.service.MainAccountApiService;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
@Order(1)
public class AccountHolderLoaderAspect {
    
    @Lazy
    @DubboReference
    private MainAccountApiService mainAccountApiService;

    @Pointcut("@annotation(plus.xyc.server.main.api.aspect.annotation.AccountHolderLoader)")
    public void accountHolderLoaderPointCut() {
    }

    @Around("accountHolderLoaderPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AccountHolderLoader annotation = method.getAnnotation(AccountHolderLoader.class);
        String value = annotation.value();

        SessionUser user = SpelExpressionUtils.evaluateExpression(value, joinPoint, SessionUser.class);
        ApiAccountResponse account = mainAccountApiService.getById(user.getId());
        log.info("AccountHolderLoaderAspect account: {}", account);
        AccountHolder.set(account);
        return joinPoint.proceed();
    }

    @AfterReturning("accountHolderLoaderPointCut()")
    public void doAfterReturning(JoinPoint joinPoint) {
        log.info("AccountHolderLoaderAspect afterReturning AccountHolder.clear()");
        AccountHolder.clear();
    }

}
