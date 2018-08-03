package core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Vant
 * @version 2018/8/3 上午 8:17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface Autowired {
    /**
     * 如果为true,未找到时抛出异常,为false,则未找到时值为null
     */
    boolean required() default true;
}