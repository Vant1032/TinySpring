package cc.vant.tinyspring.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 只有添加了Configuration的类才作为配置类,否则无效
 *
 * @author Vant
 * @since 2018/8/3 8:14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Configuration {

}
