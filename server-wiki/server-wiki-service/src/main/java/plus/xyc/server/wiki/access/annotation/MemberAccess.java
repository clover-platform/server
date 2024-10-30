package plus.xyc.server.wiki.access.annotation;

import plus.xyc.server.wiki.access.enums.AccessCode;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MemberAccess {

    AccessCode[] value() default {};

}
