package cc.vant.tinyspring.core.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定bean的产生方式,
 *
 * @author Vant
 * @since 2018/8/3 8:41
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Scope {
    @NotNull ScopeType value() default ScopeType.Singleton;
}
