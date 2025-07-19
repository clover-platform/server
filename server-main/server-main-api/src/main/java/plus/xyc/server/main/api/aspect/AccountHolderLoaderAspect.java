package plus.xyc.server.main.api.aspect;

import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
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
public class AccountHolderLoaderAspect {
    
    @DubboReference
    private MainAccountApiService mainAccountApiService;

    @Pointcut("@annotation(plus.xyc.server.main.api.aspect.annotation.AccountHolderLoader)")
    public void accountHolderLoaderPointCut() {
    }

    @Around("accountHolderLoaderPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            AccountHolderLoader annotation = method.getAnnotation(AccountHolderLoader.class);
            String value = annotation.value();

            SessionUser user = SpelExpressionUtils.evaluateExpression(value, joinPoint, SessionUser.class);
            ApiAccountResponse account = mainAccountApiService.getById(user.getId());
            AccountHolder.set(account);

            return joinPoint.proceed();
        } finally {
            AccountHolder.clear();
        }
    }

}
