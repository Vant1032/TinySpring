package cc.vant.tinyspring.core;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Vant
 * @since 2018/8/13 23:24
 */
public interface BeanGenerator {
    /**
     * 负责管理bean的创建,比如单例或prototype
     *
     * @param beanFactory 为了处理bean之间的依赖关系
     * @return bean
     */
    Object generate(QualifiableBeanFactory beanFactory) throws IllegalAccessException, InvocationTargetException, InstantiationException;

    BeanDefinition getBeanDefinition();
}
