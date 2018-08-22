package cc.vant.core;

import cc.vant.core.exception.BeanInstantiationException;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Vant
 * @version 2018/8/22 下午 2:38
 */
public class FactoryBeanGenerator implements BeanGenerator {
    FactoryBean<?> factoryBean;

    public FactoryBeanGenerator(FactoryBean<?> factoryBean) {
        this.factoryBean = factoryBean;
    }

    @Override
    public Object generate(BeanFactory beanFactory) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new BeanInstantiationException(factoryBean.getObjectType().getName());
        }
    }
}
