package cc.vant.core;

import cc.vant.core.annotations.Autowired;
import cc.vant.core.annotations.ScopeType;
import cc.vant.core.exception.BeanInstantiationException;
import cc.vant.core.exception.NoSuchBeanDefinitionException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * 只负责产生Bean
 *
 * @author Vant
 * @version 2018/8/13 下午 11:28
 */
public class DefaultBeanGenerator implements BeanGenerator {
    private Constructor<?> constructor;
    private Class<?> clazz;
    @NotNull
    private ArrayList<Field> fields = new ArrayList<>();
    private ScopeType scopeType = ScopeType.Singleton;
    private Object beanInstance;

    public DefaultBeanGenerator(Class<?> clazz) {
        this.clazz = clazz;
    }

    public DefaultBeanGenerator(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public DefaultBeanGenerator(Constructor<?> constructor, Field field) {
        this.constructor = constructor;
        fields.add(field);
    }

    public DefaultBeanGenerator(Class<?> clazz, Field field) {
        this.clazz = clazz;
        fields.add(field);
    }

    @Override
    public Object generate(@NotNull BeanFactory beanFactory) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (scopeType == ScopeType.Singleton) {
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
    private Object generateNew(@NotNull BeanFactory beanFactory) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object instance;
        if (constructor == null) {
            instance = clazz.newInstance();
        } else {
            final Class[] parameterTypes = constructor.getParameterTypes();
            final Autowired autowired = constructor.getAnnotation(Autowired.class);
            Object[] objects = new Object[parameterTypes.length];
            //对@Autowired require属性的支持
            if (parameterTypes.length > 0) {
                if (autowired.required()) {
                    for (int i = 0; i < parameterTypes.length; i++) {
                        objects[i] = beanFactory.getBean(parameterTypes[i]);
                    }
                } else {
                    for (int i = 0; i < parameterTypes.length; i++) {
                        try {
                            objects[i] = beanFactory.getBean(parameterTypes[i]);
                        } catch (@NotNull NoSuchBeanDefinitionException | BeanInstantiationException e) {
                            objects[i] = null;
                        }
                    }
                }
            }

            instance = constructor.newInstance(objects);
        }

        //构建field
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Object bean;
            if (field.getAnnotation(Autowired.class).required()) {
                bean = beanFactory.getBean(field.getType());
            } else {
                try {
                    bean = beanFactory.getBean(field.getType());
                } catch (@NotNull NoSuchBeanDefinitionException | BeanInstantiationException e) {
                    bean = null;
                }
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

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public ScopeType getScopeType() {
        return scopeType;
    }

    public void setScopeType(ScopeType scopeType) {
        this.scopeType = scopeType;
    }
}