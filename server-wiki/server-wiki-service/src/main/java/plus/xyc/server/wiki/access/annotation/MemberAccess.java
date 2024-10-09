package plus.xyc.server.wiki.access.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MemberAccess {

    int[] value() default {};

}
