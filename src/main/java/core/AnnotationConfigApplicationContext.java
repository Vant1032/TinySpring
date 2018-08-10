package core;


import core.annotations.Autowired;
import core.annotations.Bean;
import core.annotations.ComponentScan;
import core.annotations.Configuration;
import core.annotations.Scope;
import core.annotations.ScopeType;
import core.exception.BeanInstantiationException;
import core.exception.NoSuchBeanDefinitionException;
import core.exception.SpringInitException;
import core.util.SearchPackageClassUtil;
import core.util.StringUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vant
 * @version 2018/8/3 上午 12:41
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {
    private Map<String, BeanDefinition> beanMap = new HashMap<>();
    private Map<Class, String> rBeanMap = new HashMap<>();


    /**
     * 由于只支持Bean注解,所以这样处理
     *
     * @param config javaConfig类,用于配置TinySpring的类
     */
    public AnnotationConfigApplicationContext(Class config) {
        if (config.getAnnotation(Configuration.class) == null) {
            return;
        }
        ComponentScan componentScan = (ComponentScan) config.getDeclaredAnnotation(ComponentScan.class);
        if (componentScan == null) {
            return;
        }

        Class[] basePackages = componentScan.basePackages();
        for (Class basePackage : basePackages) {
            String name = basePackage.getPackage().getName();
            String[] searchPackageClass = SearchPackageClassUtil.searchPackageClass(name);

            for (String packageClass : searchPackageClass) {
                handleScannedClass(packageClass);
            }
        }

        //TODO:添加单例缓存
        for (Class beanClass : rBeanMap.keySet()) {
            final Scope scope = (Scope) beanClass.getAnnotation(Scope.class);
            //默认单例模式
            if (scope == null || scope.value() == ScopeType.Singleton) {
                //依赖getBean
            }
        }
    }

    private void handleScannedClass(String packageClass) {
        try {
            Class<?> aClass = Class.forName(packageClass);

            //处理bean
            Bean beanAnnotation = aClass.getAnnotation(Bean.class);
            if (beanAnnotation == null) return;

            BeanDefinition beanDefinition = new BeanDefinition();
            String beanName;
            if ("".equals(beanAnnotation.value())) {
                final String simpleName = aClass.getSimpleName();
                beanName = StringUtil.firstCharUpper(simpleName);
            } else {
                beanName = beanAnnotation.value();
            }
            beanDefinition.name = beanName;
            beanDefinition.clazz = aClass;
            final BeanDefinition previous = beanMap.put(beanName, beanDefinition);
            if (previous != null) {
                throw new SpringInitException("the bean named " + beanName + "is already exist");
            }
            rBeanMap.put(aClass, beanName);


            //处理autowired
            BeanWiredData wiredData = new BeanWiredData();
            beanDefinition.wiredData = wiredData;
            //Constructors(最多只能有一个Constructor可以autowired,否则报错)
            final Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
            boolean already = false;
            for (Constructor<?> declaredConstructor : declaredConstructors) {
                final Autowired autowired = declaredConstructor.getAnnotation(Autowired.class);
                if (autowired != null) {
                    if (already) {
                        throw new SpringInitException(aClass.getName() + " have multiple constructor");
                    } else {
                        wiredData.setConstructor(declaredConstructor);
                        already = true;
                    }
                }
            }

            //fields
            final Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                final Autowired autowired = declaredField.getAnnotation(Autowired.class);
                if (autowired == null) continue;
                wiredData.add(declaredField);
            }

        } catch (Throwable e) {
            throw new SpringInitException(e);
        }
    }


    /**
     * @return 如果没有找到则抛异常
     */
    @Override
    public Object getBean(String beanName) {
        if (!beanMap.containsKey(beanName)) {
            throw new NoSuchBeanDefinitionException();
        }

        //根据wiredData构建
        final BeanDefinition beanDefinition = beanMap.get(beanName);
        try {
            final BeanWiredData wiredData = beanDefinition.wiredData;
            if (wiredData == null) {
                if (beanDefinition.instance != null) {
                    return beanDefinition.instance;
                }
                return beanDefinition.clazz.newInstance();
            }

            //构建constructor
            final Constructor constructor = wiredData.getConstructor();
            Object object;
            if (constructor == null) {
                object = beanDefinition.clazz.newInstance();
            } else {
                final Class[] parameterTypes = constructor.getParameterTypes();
                Object[] objects = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    objects[i] = getBean(parameterTypes[i]);
                }
                object = constructor.newInstance(objects);
            }
            //构建field
            final ArrayList<Field> fields = wiredData.getFields();
            for (Field field : fields) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(object, getBean(field.getType()));
            }

            return object;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new BeanInstantiationException(e);
        }
    }

    /**
     * @return 若没有找到则抛异常
     */
    @Override
    public <T> T getBean(Class<T> requireType) {
        final String s = rBeanMap.get(requireType);
        if (s == null) {
            throw new NoSuchBeanDefinitionException();
        }
        return (T) getBean(s);
    }
}