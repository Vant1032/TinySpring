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
            beanMap.put(beanName, beanDefinition);
            rBeanMap.put(aClass, beanName);

            //添加scope的单例初始化缓存
            Scope scope = aClass.getAnnotation(Scope.class);
            if (scope != null && scope.value() == ScopeType.Singleton) {
                beanDefinition.instance = aClass.newInstance();
            }

            //处理autowired
            BeanWiredData wiredData = new BeanWiredData();
            beanDefinition.wiredData = wiredData;
            //fields
            final Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                final Autowired autowired = declaredField.getAnnotation(Autowired.class);
                if (autowired == null) continue;
                wiredData.add(declaredField);
            }
            //Constructors
            final Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
            for (Constructor<?> declaredConstructor : declaredConstructors) {
                //TODO:
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
        try {
            Object bean;
            if ((bean = beanMap.get(beanName).instance) != null) {
                return bean;
            }
            Class beanClass = beanMap.get(beanName).clazz;
            return beanClass.newInstance();//只支持具有无参构造器的bean
        } catch (InstantiationException | IllegalAccessException e) {
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