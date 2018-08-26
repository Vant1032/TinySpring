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
 * @since 2018/8/23 21:16
 */
public class DefaultBeanFactory implements QualifiableBeanFactory {
    private BeanContainer beanContainer;

    public DefaultBeanFactory(BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
    }

    /**
     * @return 如果没有找到返回null,否则为Bean
     */
    @Override
    @Nullable
    public Object getBean(String beanName) {
        final BeanGenerator generator = beanContainer.getGenerator(beanName);
        if (generator == null) {
            return null;
        }

        try {
            return generator.generate(this);
        } catch (@NotNull IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new BeanInstantiationException(e);
        }
    }

    /**
     * 遇到抽象类或接口时会遍历所有bean以找到子类
     * @return null若没有找到,否则为Bean
     * @throws MultipleBeanDefinition 若找到多个匹配项
     */
    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(@NotNull Class<T> requireType) {
        if (requireType.isInterface() || Modifier.isAbstract(requireType.getModifiers())) {
            return (T) getAbstractBean(requireType);
        }
        final ArrayList<String> beanNames = beanContainer.getBeanNames(requireType);
        if (beanNames == null) {
            return null;
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
                } catch (@NotNull IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    throw new NoSuchBeanDefinitionException(requireType.getName(), e);
                }
            }
        }
    }

    /**
     * 可以处理@Primary以及@Qualifier的情况
     *
     * @param requireType 只能是一个抽象类或接口的Class,不可以是具体类的,因为没有相应检查
     * @param qualifiers  可以是@Qualifier(只能有一个),也可以是被@Qualifier注释的其它注释
     * @param <T>         Bean class type
     * @return null如果没有找到,否则为Bean
     */
    @Nullable
    private <T> T getAbstractBean(@NotNull Class<T> requireType, @NotNull Annotation... qualifiers) {
        return getAbstractBean(requireType, QualifierCondition.get(qualifiers));
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
        if (result == null) return null;

        try {
            return (T) result.generate(this);
        } catch (@NotNull IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new BeanInstantiationException(requireType.getName());
        }
    }

    /**
     * @param qualifiers 包含Qualifier
     */
    @Nullable
    @Override
    public <T> T getBeanByQualifier(@NotNull Class<T> requireType, Annotation[] qualifiers) {
        return getBeanByQualifier(requireType, QualifierCondition.get(qualifiers));
    }

    /**
     * @param <T> BeanType
     * @return null如果没有找到, 否则返回Bean
     */
    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBeanByQualifier(@NotNull Class<T> requireType, @NotNull QualifierCondition condition) {
        if (requireType.isInterface() || Modifier.isAbstract(requireType.getModifiers())) {
            return getAbstractBean(requireType, condition);
        }
        final ArrayList<BeanGenerator> generators = beanContainer.getGenerators(requireType);
        if (generators.size() == 0) {
            return null;
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
        if (gen == null) {
            return null;
        }

        try {
            return (T) gen.generate(this);
        } catch (@NotNull IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new BeanInstantiationException(requireType.getName());
        }
    }


    /**
     * @param qualifiers 不可以包含非@Qualifier类型可以是@Qualifier(最多只能有一个),也可以是被@Qualifier注解的其它注解
     * @return QualifierCondition.getQualifierStr 可能为null
     */
    @NotNull
    private static QualifierCondition toQualifierCondition(@NotNull Annotation... qualifiers) {
        Annotation[] tmp = null;
        QualifierCondition condition = new QualifierCondition();
        for (int i = 0; i < qualifiers.length; i++) {
            if (qualifiers[i] instanceof Qualifier) {
                Qualifier q = (Qualifier) qualifiers[i];
                condition.setQualifierStr(q.value());
                tmp = new Annotation[qualifiers.length - 1];
                for (int j = 0; j < i; j++) {
                    tmp[j] = qualifiers[j];
                }
                for (int j = i + 1; j < qualifiers.length; j++) {
                    tmp[j - 1] = qualifiers[j];
                }
                condition.setQualifiers(tmp);
                return condition;
            }
        }
        condition.setQualifiers(qualifiers);
        return condition;
    }
}
