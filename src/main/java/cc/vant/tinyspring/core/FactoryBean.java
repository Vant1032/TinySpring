package cc.vant.tinyspring.core;

import org.jetbrains.annotations.NotNull;

/**
 * @author Vant
 * @version 2018/8/22 下午 2:33
 */
public interface FactoryBean<T> {
    @NotNull T getObject() throws Exception;

    @NotNull Class<?> getObjectType();

    boolean isSingleton();

    BeanDefinition getBeanDefinition();
}
