package cc.vant.tinyspring.core;

import cc.vant.tinyspring.core.annotations.Autowired;
import cc.vant.tinyspring.core.annotations.ScopeType;
import cc.vant.tinyspring.core.exception.NoSuchBeanDefinitionException;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * 只负责产生Bean
 *
 * @author Vant
 * @since 2018/8/13 23:28
 */
public class DefaultBeanGenerator implements BeanGenerator {
    private Constructor<?> constructor;
    @NotNull
    private ArrayList<Field> fields = new ArrayList<>();
    private Object beanInstance;
    private BeanDefinition beanDefinition;

    public DefaultBeanGenerator(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
    }

    public DefaultBeanGenerator(Constructor<?> constructor, BeanDefinition beanDefinition) {
        this.constructor = constructor;
        this.beanDefinition = beanDefinition;
    }

    public DefaultBeanGenerator(Constructor<?> constructor, Field field, BeanDefinition beanDefinition) {
        this.constructor = constructor;
        fields.add(field);
        this.beanDefinition = beanDefinition;
    }

    public DefaultBeanGenerator(Field field, BeanDefinition beanDefinition) {
        fields.add(field);
        this.beanDefinition = beanDefinition;
    }

    @Override
    public Object generate(@NotNull QualifiableBeanFactory beanFactory) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (beanDefinition.getScopeType() == ScopeType.Singleton) {
            if (beanInstance == null) {
                beanInstance = generateNew(beanFactory);
            }
            return beanInstance;
        }
        return generateNew(beanFactory);
    }

    /**
     * 在constructor不存在的情况下,clazz必须存在
     *
     * @param beanFactory 用来处理bean之间的依赖关系
     */
    @SuppressWarnings("unchecked")
    private Object generateNew(@NotNull QualifiableBeanFactory beanFactory) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object instance;
        if (constructor == null) {
            instance = beanDefinition.getType().newInstance();
        } else if (constructor.getParameterCount() == 0) {
            instance = constructor.newInstance();
        } else {
            boolean required = constructor.getAnnotation(Autowired.class).required();
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();

            Object[] objects = fillBean(required, parameterTypes, parameterAnnotations, beanFactory);

            instance = constructor.newInstance(objects);
        }

        //构建field
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Object bean;
            if (field.getAnnotation(Autowired.class).required()) {
                if (QualifierCondition.get(field.getAnnotations()).empty()) {
                    bean = beanFactory.getBean(field.getType());
                } else {
                    bean = beanFactory.getBeanByQualifier(field.getType());
                }

                if (bean == null) {
                    throw new NoSuchBeanDefinitionException(field.getType().getName());
                }
            } else {
                bean = beanFactory.getBean(field.getType());
            }
            field.set(instance, bean);
        }
        return instance;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public void addField(Field field) {
        fields.add(field);
    }

    @Override
    public BeanDefinition getBeanDefinition() {
        return beanDefinition;
    }

    public void setBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
    }


    /**
     * 提供对@Qualifier的支持来查找一组Bean
     *
     * @param required     true如果找不到Bean抛异常,false则找不到Bean用null填充
     * @param requireTypes 待填充的Bean的class类型
     * @param annotations  里面包含各个Bean所需的Qualifier,顺序与requireTypes一致
     * @param beanFactory  用于处理依赖
     * @return Beans
     */
    @SuppressWarnings("unchecked")
    public static Object[] fillBean(boolean required, Class[] requireTypes, Annotation[][] annotations, QualifiableBeanFactory beanFactory) {
        Object[] objects = new Object[requireTypes.length];
        //对@Autowired require属性的支持
        if (requireTypes.length > 0) {
            //参数上标注Qualifier的解决方案
            if (required) {
                for (int i = 0; i < requireTypes.length; i++) {
                    if (annotations[i].length == 0) {
                        objects[i] = beanFactory.getBean(requireTypes[i]);
                    } else {
                        objects[i] = beanFactory.getBeanByQualifier(requireTypes[i], annotations[i]);
                    }
                    if (objects[i] == null) {
                        throw new NoSuchBeanDefinitionException(requireTypes[i].getName());
                    }
                }
            } else {
                for (int i = 0; i < requireTypes.length; i++) {
                    objects[i] = beanFactory.getBean(requireTypes[i]);
                }
            }
        }
        return objects;
    }
}