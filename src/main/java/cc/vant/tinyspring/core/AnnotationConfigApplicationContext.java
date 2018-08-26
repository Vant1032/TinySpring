package cc.vant.tinyspring.core;


import cc.vant.tinyspring.core.annotations.Autowired;
import cc.vant.tinyspring.core.annotations.Bean;
import cc.vant.tinyspring.core.annotations.ComponentScan;
import cc.vant.tinyspring.core.annotations.Configuration;
import cc.vant.tinyspring.core.annotations.Primary;
import cc.vant.tinyspring.core.annotations.Qualifier;
import cc.vant.tinyspring.core.annotations.Scope;
import cc.vant.tinyspring.core.annotations.ScopeType;
import cc.vant.tinyspring.core.exception.MultipleBeanDefinition;
import cc.vant.tinyspring.core.exception.SpringInitException;
import cc.vant.tinyspring.core.util.SearchPackageClassUtil;
import cc.vant.tinyspring.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO:处理Autowired里面的getBean方法,让@Qualifier完全实现
 * 持有DefaultBeanFactory并用委托来产生Bean的形式
 * 添加了@Autowired接口和抽象类支持
 *
 * @author Vant
 * @since 2018/8/3 12:41
 */
public class AnnotationConfigApplicationContext implements BeanFactory, AutoCloseable {
    @NotNull
    private BeanContainer beanContainer = new BeanContainer();

    @NotNull
    private DefaultBeanFactory beanFactory = new DefaultBeanFactory(beanContainer);

    /**
     * @param configs javaConfig类,用于配置TinySpring的类,该类必须用@Configuration注解
     */
    public AnnotationConfigApplicationContext(@NotNull Class<?>... configs) {
        Set<String> scannedPackage = new HashSet<>();
        for (Class<?> config : configs) {
            if (config.isAnnotationPresent(Configuration.class)) {
                throw new SpringInitException(config.getName() + " is not annotated by Configuration");
            }

            //添加@Bean@Scope放在方法上的支持
            handleConfigBean(config);
            ComponentScan componentScan = config.getDeclaredAnnotation(ComponentScan.class);
            if (componentScan != null) {
                scannedPackage.addAll(scanPackage(componentScan));
            }
        }
        for (String clazz : scannedPackage) {
            handleScannedClass(clazz);
        }
    }

    private void handleConfigBean(@NotNull Class<?> config) {
        Object cfgInstance;
        try {
            cfgInstance = config.newInstance();
        } catch (@NotNull InstantiationException | IllegalAccessException e) {
            throw new SpringInitException("can not instantiate " + config.getName(), e);
        }
        final Method[] methods = config.getDeclaredMethods();
        for (Method method : methods) {
            final Bean bean = method.getAnnotation(Bean.class);
            if (method.getParameterCount() > 0 && method.isAnnotationPresent(Autowired.class)) {
                throw new SpringInitException(method.getName() + " should use @Autowired");
            }
            if (bean != null) {
                final DefaultBeanDefinition beanDefinition = new DefaultBeanDefinition();
                final ConfigBeanGenerator generator = new ConfigBeanGenerator(cfgInstance, method, beanDefinition);
                String beanName = StringUtils.generateBeanName(beanContainer, bean, method.getReturnType());
                final Scope scope = method.getAnnotation(Scope.class);
                if (scope != null && scope.value() == ScopeType.Prototype) {
                    beanDefinition.setScopeType(ScopeType.Prototype);
                }
                beanDefinition.setBeanName(beanName);
                beanDefinition.setPrimary(method.isAnnotationPresent(Primary.class));
                resolveQualifier(method.getAnnotations(), beanDefinition);
                beanDefinition.setType(method.getReturnType());
                beanContainer.addBean(beanDefinition, generator);
            }
        }
    }

    /**
     * @return 所有扫描到的类, 已经去重
     */
    @NotNull
    private Set<String> scanPackage(@NotNull ComponentScan... componentScans) {
        //由于包扫描会扫描子包,所以如果有子包存在,就删除,防止重复扫描,降低开销
        Set<String> packageNames = new HashSet<>();
        for (ComponentScan componentScan : componentScans) {
            Class[] basePackages = componentScan.basePackageClasses();
            for (Class basePackage : basePackages) {
                String name = basePackage.getPackage().getName();
                packageNames.add(name);
            }
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
        return searchClass(uniquePackage);
    }

    /**
     * @return className
     */
    @NotNull
    private Set<String> searchClass(@NotNull String[] uniquePackage) {
        Set<String> packClass = new HashSet<>();
        for (String pac : uniquePackage) {
            String[] searchPackageClass = SearchPackageClassUtil.searchPackageClass(pac);
            Collections.addAll(packClass, searchPackageClass);
        }
        return packClass;
    }

    private void handleScannedClass(String packageClass) {

        Class<?> beanClass;
        try {
            beanClass = Class.forName(packageClass);
        } catch (ClassNotFoundException e) {
            throw new SpringInitException(e);
        }

        //处理bean
        Bean beanAnnotation = beanClass.getAnnotation(Bean.class);
        if (beanAnnotation == null) return;

        String beanName = StringUtils.generateBeanName(beanContainer, beanAnnotation, beanClass);

        //处理autowired
        //Constructors(最多只能有一个Constructor可以autowired,否则报错)
        DefaultBeanGenerator generator;
        final Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        boolean already = false;
        Constructor<?> constructor = null;
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            if (declaredConstructor.isAnnotationPresent(Autowired.class)) {
                if (already) {
                    throw new SpringInitException(beanClass.getName() + " have multiple constructor");
                } else {
                    constructor = declaredConstructor;
                    already = true;
                }
            }
        }
        final DefaultBeanDefinition beanDefinition = new DefaultBeanDefinition();
        if (constructor == null) {
            generator = new DefaultBeanGenerator(beanDefinition);
        } else {
            generator = new DefaultBeanGenerator(constructor, beanDefinition);
        }
        //fields
        final Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (!declaredField.isAnnotationPresent(Autowired.class)) continue;
            generator.addField(declaredField);
        }

        //scope
        final Scope scope = beanClass.getAnnotation(Scope.class);
        if (scope != null && scope.value() == ScopeType.Prototype) {
            beanDefinition.setScopeType(ScopeType.Prototype);
        }
        beanDefinition.setBeanName(beanName);
        beanDefinition.setType(beanClass);
        beanDefinition.setPrimary(beanClass.isAnnotationPresent(Primary.class));
        resolveQualifier(beanClass.getAnnotations(), beanDefinition);
        beanContainer.addBean(beanDefinition, generator);
    }

    private void resolveQualifier(@NotNull Annotation[] annotations, @NotNull DefaultBeanDefinition beanDefinition) {
        final ArrayList<Annotation> qualifiers = new ArrayList<>();
        beanDefinition.setQualifiers(qualifiers);

        //qualifier
        for (Annotation annotation : annotations) {
            if (annotation.getClass().isAnnotationPresent(Qualifier.class)) {
                qualifiers.add(annotation);
            } else if (annotation instanceof Qualifier) {
                Qualifier qualifier = (Qualifier) annotation;
                beanDefinition.setQualifierString(qualifier.value());
            }
        }
    }


    /**
     * @return null若没有找到,否则为Bean
     * @throws MultipleBeanDefinition 若找到多个匹配项
     */
    @Nullable
    @Override
    public Object getBean(String beanName) {
        return beanFactory.getBean(beanName);
    }

    /**
     * 遇到抽象类或接口时会遍历所有bean以找到子类
     *
     * @return null若没有找到, 否则为Bean
     * @throws MultipleBeanDefinition 若找到多个匹配项
     */
    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(@NotNull Class<T> requireType) {
        return beanFactory.getBean(requireType);
    }

    public ArrayList<String> getBeanNames(Class<?> clazz) {
        return beanContainer.getBeanNames(clazz);
    }


    @Override
    public void close() throws Exception {
        beanContainer.clear();
    }
}