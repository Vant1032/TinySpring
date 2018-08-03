package core;


import core.annotations.Bean;
import core.annotations.ComponentScan;
import core.annotations.Configuration;
import core.annotations.Scope;
import core.annotations.ScopeType;
import core.util.SearchPackageClassUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vant
 * @version 2018/8/3 上午 12:41
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {
    private Map<String, Class> beanMap = new HashMap<>();
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
        if (componentScan != null) {
            Class[] basePackages = componentScan.basePackages();
            for (Class basePackage : basePackages) {
                String name = basePackage.getPackage().getName();
                String[] searchPackageClass = SearchPackageClassUtil.searchPackageClass(name);

                for (String packageClass : searchPackageClass) {
                    try {
                        Class<?> aClass = Class.forName(packageClass);
                        Bean beanAnnotation = aClass.getAnnotation(Bean.class);
                        String beanName = "";
                        if (beanAnnotation != null) {
                            beanName = beanAnnotation.value();
                        } else {
                            final char[] chars = aClass.getSimpleName().toCharArray();
                            chars[0] = Character.toUpperCase(chars[0]);
                            beanName = new String(chars);
                        }
                        beanMap.put(beanName, aClass);

                        //添加scope的单例初始化缓存
                        Scope scope = aClass.getAnnotation(Scope.class);
                        if (scope != null && scope.value() == ScopeType.Singleton) {
                            singletonBeanCache.put(beanName, aClass.newInstance());
                        }

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @return 如果没有找到则返回null
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
            e.printStackTrace();
        }
        return null;
    }
}
