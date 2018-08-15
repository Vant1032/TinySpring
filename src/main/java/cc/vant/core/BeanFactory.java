package cc.vant.core;

import org.jetbrains.annotations.NotNull;

/**
 * @author Vant
 * @version 2018/8/14 上午 9:51
 */
public interface BeanFactory {
    Object getBean(String beanName);

    @NotNull <T> T getBean(Class<T> requireType);
}