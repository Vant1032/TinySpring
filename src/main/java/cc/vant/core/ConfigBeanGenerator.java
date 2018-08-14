package cc.vant.core;

import cc.vant.core.annotations.ScopeType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 用于通过Config上的方法来生成bean
 *
 * @author Vant
 * @version 2018/8/13 下午 11:29
 */
public class ConfigBeanGenerator implements BeanGenerator {
    private Object configInstance;
    private Method method;
    private ScopeType scopeType = ScopeType.Singleton;
    private Object beanInstance;

    /**
     * @param method 如果method有参数,必须保证method被@autowired注解
     */
    public ConfigBeanGenerator(Object configInstance, Method method) {
        this.configInstance = configInstance;
        this.method = method;
    }

    @Override
    public Object generate(BeanFactory beanFactory) throws IllegalAccessException, InvocationTargetException {
        if (scopeType == ScopeType.Singleton) {
            if (beanInstance == null) {
                beanInstance = generateNew(beanFactory);
            }
            return beanInstance;
        }
        return generateNew(beanFactory);
    }

    public Object generateNew(BeanFactory beanFactory) throws InvocationTargetException, IllegalAccessException {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = beanFactory.getBean(parameterTypes[i]);
        }
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method.invoke(configInstance, args);
    }

    public ScopeType getScopeType() {
        return scopeType;
    }

    public void setScopeType(ScopeType scopeType) {
        this.scopeType = scopeType;
    }
}
