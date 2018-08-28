package cc.vant.tinyspring.core.annotations;

import java.lang.annotation.*;

/**
 * @author Vant
 * @since 2018/8/26 20:33
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {
    String value() default "";
}
