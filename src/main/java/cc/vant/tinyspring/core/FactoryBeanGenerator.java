package cc.vant.tinyspring.core;

import cc.vant.tinyspring.core.exception.BeanInstantiationException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Vant
 * @version 2018/8/22 下午 2:38
 */
public class FactoryBeanGenerator implements BeanGenerator {
    private FactoryBean<?> factoryBean;

    public FactoryBeanGenerator(FactoryBean<?> factoryBean) {
        this.factoryBean = factoryBean;
    }

    @NotNull
    @Override
    public Object generate(BeanFactory beanFactory) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new BeanInstantiationException(factoryBean.getObjectType().getName());
        }
    }

    @Override
    public BeanDefinition getBeanDefinition() {
        return factoryBean.getBeanDefinition();
    }
}
