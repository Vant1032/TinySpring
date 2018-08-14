package cc.vant.core;

import cc.vant.core.annotations.ScopeType;

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
    public Object generate(BeanFactory beanFactory) throws IllegalAccessException, InvocationTargetException, InstantiationException {
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
    private Object generateNew(BeanFactory beanFactory) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object instance;
        if (constructor == null) {
            instance = clazz.newInstance();
        } else {
            final Class[] parameterTypes = constructor.getParameterTypes();
            Object[] objects = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                objects[i] = beanFactory.getBean(parameterTypes[i]);
            }
            instance = constructor.newInstance(objects);
        }

        //构建field
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(instance, beanFactory.getBean(field.getType()));
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