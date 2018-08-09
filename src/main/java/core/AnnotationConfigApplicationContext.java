package core;


import core.annotations.Autowired;
import core.annotations.Bean;
import core.annotations.ComponentScan;
import core.annotations.Configuration;
import core.annotations.Scope;
import core.annotations.ScopeType;
import core.exception.BeanInstantiationException;
import core.exception.NoSuchBeanDefinitionException;
import core.util.SearchPackageClassUtil;
import core.util.StringUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vant
 * @version 2018/8/3 上午 12:41
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {
    private Map<String, Class> beanMap = new HashMap<>();
    private Map<Class, String> rBeanMap = new HashMap<>();
    private Map<String, Object> singletonBeanCache = new HashMap<>();


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
                try {
                    Class<?> aClass = Class.forName(packageClass);

                    //处理bean
                    Bean beanAnnotation = aClass.getAnnotation(Bean.class);
                    if (beanAnnotation == null) continue;

                    String beanName;
                    if ("".equals(beanAnnotation.value())) {
                        final String simpleName = aClass.getSimpleName();
                        beanName = StringUtil.firstCharUpper(simpleName);
                    } else {
                        beanName = beanAnnotation.value();
                    }
                    beanMap.put(beanName, aClass);
                    rBeanMap.put(aClass, beanName);

                    //添加scope的单例初始化缓存
                    Scope scope = aClass.getAnnotation(Scope.class);
                    if (scope != null && scope.value() == ScopeType.Singleton) {
                        singletonBeanCache.put(beanName, aClass.newInstance());
                    }

                    //处理autowired
                    final Field[] declaredFields = aClass.getDeclaredFields();
                    for (Field declaredField : declaredFields) {
                        final Autowired autowired = declaredField.getAnnotation(Autowired.class);
                        if (autowired == null) continue;

                        if (autowired.required()) {

                        } else {

                        }
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * @return 如果没有找到则抛异常
     */
    @Override
    public Object getBean(String beanName) {
        try {
            Object bean;
            if ((bean = singletonBeanCache.get(beanName)) != null) {
                return bean;
            }
            Class beanClass = beanMap.get(beanName);
            return beanClass.newInstance();//只支持具有无参构造器的bean
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException();
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

        final Scope scope = requireType.getAnnotation(Scope.class);
        if (scope != null && scope.value() == ScopeType.Singleton) {
            final Object o = singletonBeanCache.get(requireType);
            if (o != null) {
                return (T) o;
            } else {
                throw new NoSuchBeanDefinitionException(s);
            }
        }


        if (!beanMap.containsKey(s)) {
            throw new NoSuchBeanDefinitionException(s);
        }
        try {
            return (T) beanMap.get(s).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException();
        }
    }
}