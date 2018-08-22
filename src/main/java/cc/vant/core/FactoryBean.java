package cc.vant.core;

/**
 * @author Vant
 * @version 2018/8/22 下午 2:33
 */
public interface FactoryBean<T> {
    T getObject() throws Exception;

    Class<?> getObjectType();

    boolean isSingleton();
}
