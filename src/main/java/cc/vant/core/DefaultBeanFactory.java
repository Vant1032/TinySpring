package cc.vant.core;

import cc.vant.core.exception.BeanInstantiationException;
import cc.vant.core.exception.MultipleBeanDefinition;
import cc.vant.core.exception.NoSuchBeanDefinitionException;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * @author Vant
 * @version 2018/8/23 下午 9:16
 */
public class DefaultBeanFactory implements BeanFactory{
    private BeanContainer beanContainer;

    public DefaultBeanFactory(BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
    }

    /**
     * @return 如果没有找到则抛异常
     */
    @Override
    public Object getBean(String beanName) {
        final BeanGenerator generator = beanContainer.getGenerator(beanName);
        if (generator == null) {
            throw new NoSuchBeanDefinitionException();
        }

        try {
            return generator.generate(this);
        } catch (@NotNull IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new BeanInstantiationException(e);
        }
    }

    /**
     * 遇到抽象类或接口时会遍历所有bean以找到子类
     *
     * @return 若没有找到则抛异常
     * @throws cc.vant.core.exception.MultipleBeanDefinition 若找到多个匹配项
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(@NotNull Class<T> requireType) {
        if (requireType.isInterface() || Modifier.isAbstract(requireType.getModifiers())) {
            return (T) resolveAbstract(requireType);
        }
        final ArrayList<String> beanNames = beanContainer.getBeanNames(requireType);
        if (beanNames == null) {
            throw new NoSuchBeanDefinitionException(requireType.getName());
        } else {
            if (beanNames.size() == 1) {
                try {
                    return (T) beanContainer.getGenerator(beanNames.get(0)).generate(this);
                } catch (@NotNull IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    throw new BeanInstantiationException(beanNames.get(0) + " instantiate error ", e);
                }
            } else {
                //提供对@Primary的支持
                BeanGenerator result = null;
                for (String beanName : beanNames) {
                    final BeanGenerator beanGenerator = beanContainer.getGenerator(beanName);
                    if (beanGenerator.getBeanDefinition().isPrimary()){
                        if (result == null) {
                            result = beanGenerator;
                        } else {
                            throw new MultipleBeanDefinition(requireType.getName());
                        }
                    }
                }
                try {
                    return (T) result.generate(this);
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    throw new NoSuchBeanDefinitionException(requireType.getName(), e);
                }
            }
        }
    }

    @NotNull
    private <T> Object resolveAbstract(@NotNull Class<T> requireType) {
        boolean unique = true;
        Object beanInstance = null;
        for (Class<?> beanClass : beanContainer.getClasses()) {
            if (requireType.isAssignableFrom(beanClass)) {
                if (unique) {
                    unique = false;
                    beanInstance = getBean(beanClass);
                } else {
                    throw new MultipleBeanDefinition(requireType.getName());
                }
            }
        }
        if (beanInstance == null) {
            throw new NoSuchBeanDefinitionException(requireType.getName());
        }
        return beanInstance;
    }

    public <T> T getBeanByQualifier(Class<T> requireType, Annotation... qualifiers) {
        if (requireType.isInterface() || Modifier.isAbstract(requireType.getModifiers())) {
            return (T) resolveAbstract(requireType);
        }

    }
}
