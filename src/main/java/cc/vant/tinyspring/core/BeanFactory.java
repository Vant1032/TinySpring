package cc.vant.tinyspring.core;

import org.jetbrains.annotations.NotNull;

/**
 * @author Vant
 * @since 2018/8/14 9:51
 */
public interface BeanFactory {
    Object getBean(String beanName);

    /**
     * @return 找不到Bean时返回Null还是抛异常依赖于实现
     */
    <T> T getBean(Class<T> requireType);
}