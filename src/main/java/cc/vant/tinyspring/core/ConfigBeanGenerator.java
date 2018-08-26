package cc.vant.tinyspring.core;

import cc.vant.tinyspring.core.annotations.Autowired;
import cc.vant.tinyspring.core.annotations.ScopeType;
import cc.vant.tinyspring.core.exception.NoSuchBeanDefinitionException;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 用于通过Config上的方法来生成bean
 *
 * @author Vant
 * @since 2018/8/13 23:29
 */
public class ConfigBeanGenerator implements BeanGenerator {
    private Object configInstance;
    private Method method;
    private Object beanInstance;
    private BeanDefinition beanDefinition;

    /**
     * @param method 如果method有参数,必须保证method被@autowired注解
     */
    public ConfigBeanGenerator(Object configInstance, Method method, BeanDefinition beanDefinition) {
        this.configInstance = configInstance;
        this.method = method;
        this.beanDefinition = beanDefinition;
    }

    @Override
    public Object generate(@NotNull QualifiableBeanFactory beanFactory) throws IllegalAccessException, InvocationTargetException {
        if (beanDefinition.getScopeType() == ScopeType.Singleton) {
            if (beanInstance == null) {
                beanInstance = generateNew(beanFactory);
            }
            return beanInstance;
        }
        return generateNew(beanFactory);
    }

    public Object generateNew(@NotNull QualifiableBeanFactory beanFactory) throws InvocationTargetException, IllegalAccessException {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 0) {
            //添加@Qualifier支持
            boolean required = method.getAnnotation(Autowired.class).required();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            Object[] args = DefaultBeanGenerator.fillBean(required, parameterTypes, parameterAnnotations, beanFactory);

            return method.invoke(configInstance, args);
        }
        return method.invoke(configInstance);
    }

    @Override
    public BeanDefinition getBeanDefinition() {
        return beanDefinition;
    }

    public void setBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
    }
}
