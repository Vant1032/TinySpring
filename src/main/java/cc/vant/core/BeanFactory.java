package cc.vant.core;

/**
 * @author Vant
 * @version 2018/8/14 上午 9:51
 */
public interface BeanFactory {
    Object getBean(String beanName);

    <T> T getBean(Class<T> requireType);
}