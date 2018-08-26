package cc.vant.tinyspring.core.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Vant
 * @since 2018/8/3 12:44
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ComponentScan {
    @NotNull Class[] basePackageClasses();
}
