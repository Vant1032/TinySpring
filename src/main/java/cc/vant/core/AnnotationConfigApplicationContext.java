package cc.vant.core;


import cc.vant.core.annotations.Autowired;
import cc.vant.core.annotations.Bean;
import cc.vant.core.annotations.ComponentScan;
import cc.vant.core.annotations.Configuration;
import cc.vant.core.annotations.Scope;
import cc.vant.core.annotations.ScopeType;
import cc.vant.core.exception.BeanInstantiationException;
import cc.vant.core.exception.NoSuchBeanDefinitionException;
import cc.vant.core.exception.SpringInitException;
import cc.vant.core.util.SearchPackageClassUtil;
import cc.vant.core.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Vant
 * @version 2018/8/3 上午 12:41
 */
public class AnnotationConfigApplicationContext implements BeanFactory {
    /**
     * @param configs javaConfig类,用于配置TinySpring的类,该类必须用@Configuration注解
     */
    public AnnotationConfigApplicationContext(Class<?>... configs) {
        Set<String> scanedPackage = new HashSet<>();
        for (Class<?> config : configs) {
            if (config.getAnnotation(Configuration.class) == null) {
                throw new SpringInitException(config.getName() + " is not annotated by Configuration");
            }

            //添加@Bean@Scope放在方法上的支持
            handleConfigBean(config);
            ComponentScan componentScan = config.getDeclaredAnnotation(ComponentScan.class);
            if (componentScan != null) {
                scanedPackage.addAll(scanPackage(componentScan));
            }
        }
        for (String clazz : scanedPackage) {
            handleScannedClass(clazz);
        }
    }

    private void handleConfigBean(Class<?> config) {
        Object cfgInstance;
        try {
            cfgInstance = config.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SpringInitException("can not instantiate " + config.getName(), e);
        }
        final Method[] methods = config.getDeclaredMethods();
        for (Method method : methods) {
            final Bean bean = method.getAnnotation(Bean.class);
            if (bean != null) {
                String beanName;
                if ("".equals(bean.value())) {
                    beanName = StringUtils.firstCharLower(method.getReturnType().getSimpleName());
                } else {
                    beanName = bean.value();
                }
                final ConfigBeanGenerator generator = new ConfigBeanGenerator(cfgInstance, method);
                final Scope scope = method.getAnnotation(Scope.class);
                if (scope != null && scope.value() == ScopeType.Prototype) {
                    generator.setScopeType(ScopeType.Prototype);
                }
                BeanContainer.addBean(beanName, method.getReturnType(), generator);
            }
        }
    }

    /**
     * TODO:将此函数分解用以支持更优的搜索方式
     * @return 所有扫描到的类, 已经去重
     */
    private Set<String> scanPackage(ComponentScan componentScan) {
        //由于包扫描会扫描子包,所以如果有子包存在,就删除,防止重复扫描,降低开销
        Class[] basePackages = componentScan.basePackageClasses();
        Set<String> packageNames = new HashSet<>();
        for (Class basePackage : basePackages) {
            String name = basePackage.getPackage().getName();
            packageNames.add(name);
        }
        final String[] nameStrs = packageNames.toArray(new String[0]);
        int size = 0;
        for (int i = 0; i < nameStrs.length; i++) {
            if (nameStrs[i] == null) continue;
            for (int j = i + 1; j < nameStrs.length; j++) {
                if (nameStrs[j] == null) continue;
                if (nameStrs[i].contains(nameStrs[j])) {
                    nameStrs[i] = null;
                    size++;
                    break;
                } else if (nameStrs[j].contains(nameStrs[i])) {
                    nameStrs[j] = null;
                    size++;
                }
            }
        }
        size = nameStrs.length - size;
        String[] uniquePackage = new String[size];
        for (int i = 0; i < nameStrs.length; i++) {
            if (nameStrs[i] != null) {
                uniquePackage[--size] = nameStrs[i];
            }
        }
        //处理重复包注解
        Set<String> packClass = new HashSet<>();
        for (String pac : uniquePackage) {
            String[] searchPackageClass = SearchPackageClassUtil.searchPackageClass(pac);
            Collections.addAll(packClass, searchPackageClass);
        }
        return packClass;
    }

    private void handleScannedClass(String packageClass) {
        try {
            Class<?> beanClass = Class.forName(packageClass);

            //处理bean
            Bean beanAnnotation = beanClass.getAnnotation(Bean.class);
            if (beanAnnotation == null) return;

            String beanName;
            if ("".equals(beanAnnotation.value())) {
                final String simpleName = beanClass.getSimpleName();
                beanName = StringUtils.firstCharLower(simpleName);
            } else {
                beanName = beanAnnotation.value();
            }


            //处理autowired
            //Constructors(最多只能有一个Constructor可以autowired,否则报错)
            DefaultBeanGenerator generator;
            final Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
            boolean already = false;
            Constructor<?> constructor = null;
            for (Constructor<?> declaredConstructor : declaredConstructors) {
                final Autowired autowired = declaredConstructor.getAnnotation(Autowired.class);
                if (autowired != null) {
                    if (already) {
                        throw new SpringInitException(beanClass.getName() + " have multiple constructor");
                    } else {
                        constructor = declaredConstructor;
                        already = true;
                    }
                }
            }
            if (constructor == null) {
                generator = new DefaultBeanGenerator(beanClass);
            } else {
                generator = new DefaultBeanGenerator(constructor);
            }
            //fields
            final Field[] declaredFields = beanClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                final Autowired autowired = declaredField.getAnnotation(Autowired.class);
                if (autowired == null) continue;
                generator.addField(declaredField);
            }

            //scope
            final Scope scope = beanClass.getAnnotation(Scope.class);
            if (scope != null && scope.value() == ScopeType.Prototype) {
                generator.setScopeType(ScopeType.Prototype);
            }
            BeanContainer.addBean(beanName, beanClass, generator);
        } catch (Throwable e) {
            throw new SpringInitException(e);
        }
    }


    /**
     * @return 如果没有找到则抛异常
     */
    @Override
    public Object getBean(String beanName) {
        final BeanGenerator generator = BeanContainer.getGenerator(beanName);
        if (generator == null) {
            throw new NoSuchBeanDefinitionException();
        }

        try {
            return generator.generate(this);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new BeanInstantiationException(e);
        }
    }

    /**
     * @return 若没有找到则抛异常
     */
    @Override
    public <T> T getBean(Class<T> requireType) {
        final ArrayList<String> beanNames = BeanContainer.getBeanNames(requireType);
        if (beanNames == null) {
            throw new NoSuchBeanDefinitionException();
        } else {
            if (beanNames.size() == 1) {
                try {
                    return (T) BeanContainer.getGenerator(beanNames.get(0)).generate(this);
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    throw new BeanInstantiationException(beanNames.get(0) + " instantiate error ");
                }
            }
            throw new BeanInstantiationException("the bean is not unique");
        }
    }
}