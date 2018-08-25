package cc.vant.tinyspring.core;

import cc.vant.tinyspring.core.annotations.Qualifier;
import cc.vant.tinyspring.core.exception.BeanInstantiationException;
import cc.vant.tinyspring.core.exception.MultipleBeanDefinition;
import cc.vant.tinyspring.core.exception.NoSuchBeanDefinitionException;
import cc.vant.tinyspring.core.exception.SpringInitException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * @author Vant
 * @version 2018/8/23 下午 9:16
 */
public class DefaultBeanFactory implements QualifiableBeanFactory {
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
     * @throws MultipleBeanDefinition 若找到多个匹配项
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(@NotNull Class<T> requireType) {
        if (requireType.isInterface() || Modifier.isAbstract(requireType.getModifiers())) {
            return (T) getAbstractBean(requireType);
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
                    if (beanGenerator.getBeanDefinition().isPrimary()) {
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

    /**
     * 可以处理@Primary以及@Qualifier的情况
     *
     * @param requireType
     * @param qualifiers  可以是@Qualifier(只能有一个),也可以是被@Qualifier注释的其它注释
     * @param <T>         Bean class type
     * @return Bean
     */
    private <T> T getAbstractBean(@NotNull Class<T> requireType, @NotNull Annotation... qualifiers) {
        return getAbstractBean(requireType, toQualifierCondition(qualifiers));
    }

    /**
     * @return null如果没有找到, 否则返回Bean
     */
    @SuppressWarnings("unchecked")
    @Nullable
    private <T> T getAbstractBean(@NotNull Class<?> requireType, @NotNull QualifierCondition condition) {
        boolean primary = false;
        BeanGenerator result = null;

        for (Class<?> beanClass : beanContainer.getClasses()) {
            if (requireType.isAssignableFrom(beanClass)) {
                //搜索各种条件的bean
                for (BeanGenerator generator : beanContainer.getGenerators(beanClass)) {
                    final BeanDefinition beanDefinition = generator.getBeanDefinition();
                    if (condition.matchQualifier(beanDefinition)) {
                        if (beanDefinition.isPrimary()) {
                            if (primary) {
                                throw new SpringInitException("multiple primary annotation");
                            }
                            primary = true;
                        }
                        result = generator;
                    }
                }
            }
        }

        if (result == null && condition.getQualifierStr() != null) {
            //找不到相关的,就通过@Qualifier的值作为BeanName尝试寻找
            result = beanContainer.getGenerator(condition.getQualifierStr());
        }
        if (result == null) throw new NoSuchBeanDefinitionException(requireType.getName());

        try {
            return (T) result.generate(this);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new BeanInstantiationException(requireType.getName());
        }
    }

    @Override
    public <T> T getBeanByQualifier(Class<T> requireType, Annotation... qualifiers) {
        return getBeanByQualifier(requireType, toQualifierCondition(qualifiers));
    }

    /**
     * @param <T> BeanType
     * @return null如果没有找到, 否则返回Bean
     */
    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBeanByQualifier(Class<T> requireType, QualifierCondition condition) {
        if (requireType.isInterface() || Modifier.isAbstract(requireType.getModifiers())) {
            return getAbstractBean(requireType, condition);
        }
        final ArrayList<BeanGenerator> generators = beanContainer.getGenerators(requireType);
        if (generators.size() == 0) {
            throw new NoSuchBeanDefinitionException(requireType.getName());
        }
        BeanGenerator gen = null;
        boolean primary = false;
        for (BeanGenerator generator : generators) {
            BeanDefinition def = generator.getBeanDefinition();
            if (condition.matchQualifier(def)) {
                if (def.isPrimary()) {
                    if (primary) {
                        throw new MultipleBeanDefinition("multiple Primary Bean");
                    }
                    primary = true;
                }
                gen = generator;
            }
        }
        if (gen == null && condition.getQualifierStr() != null) {
            gen = beanContainer.getGenerator(condition.getQualifierStr());

        }
        if (gen == null) throw new NoSuchBeanDefinitionException(requireType.getName());

        try {
            return (T) gen.generate(this);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new BeanInstantiationException(requireType.getName());
        }
    }


    /**
     * @param qualifiers 可以是@Qualifier(最多只能有一个),也可以是被@Qualifier注解的其它注解
     * @return QualifierCondition.getQualifierStr 可能为null
     */
    private static QualifierCondition toQualifierCondition(@NotNull Annotation... qualifiers) {
        Annotation[] tmp = null;
        QualifierCondition condition = new QualifierCondition();
        for (int i = 0; i < qualifiers.length; i++) {
            if (qualifiers[i] instanceof Qualifier) {
                Qualifier q = (Qualifier) qualifiers[i];
                condition.setQualifierStr(q.value());
                tmp = new Annotation[qualifiers.length - 1];
                System.arraycopy(qualifiers, 0, tmp, 0, i - 1);
                System.arraycopy(qualifiers, i + 1, tmp, i, qualifiers.length - i - 1);
                condition.setQualifiers(tmp);
                return condition;
            }
        }
        condition.setQualifiers(qualifiers);
        return condition;
    }
}
