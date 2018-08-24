package cc.vant.tinyspring.core;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Vant
 * @version 2018/8/13 下午 11:18
 */
public class BeanContainer implements AutoCloseable {
    private final Map<String, BeanGenerator> beanMap = new ConcurrentHashMap<>(256);
    private final Map<Class<?>, ArrayList<String>> rBeanMap = new ConcurrentHashMap<>(256);

    public void addBean(BeanDefinition beanDefinition, BeanGenerator beanGenerator) {
        String beanName = beanDefinition.getBeanName();
        Class<?> clazz = beanDefinition.getType();
        beanMap.put(beanName, beanGenerator);
        final ArrayList<String> beanNames = rBeanMap.get(clazz);
        if (beanNames == null) {
            final ArrayList<String> arrayList = new ArrayList<>(1);
            arrayList.add(beanName);
            rBeanMap.put(clazz, arrayList);
        } else {
            beanNames.add(beanName);
        }

    }

    /**
     * @param beanName 不可为null
     * @return 如果没有此bean, 返回null
     */
    public BeanGenerator getGenerator(String beanName) {
        return beanMap.get(beanName);
    }

    /**
     * @return 若没有此class的bean, 则返回null
     */
    public ArrayList<String> getBeanNames(Class<?> clazz) {
        return rBeanMap.get(clazz);
    }

    @NotNull
    public Set<Class<?>> getClasses() {
        return rBeanMap.keySet();
    }

    /**
     * @return 如果没有找到则返回空ArrayList
     */
    public ArrayList<BeanGenerator> getGenerators(Class<?> beanClass) {
        final ArrayList<String> strings = rBeanMap.get(beanClass);
        if (strings == null) return new ArrayList<>();
        ArrayList<BeanGenerator> generators = new ArrayList<>(strings.size());
        for (String string : strings) {
            generators.add(beanMap.get(string));
        }
        return generators;
    }

    public BeanDefinition getBeanDefinition(String beanName) {
        return getGenerator(beanName).getBeanDefinition();
    }

    public List<BeanDefinition> getBeanDefinitions(Class<?> beanClass) {
        final ArrayList<String> strings = rBeanMap.get(beanClass);
        ArrayList<BeanDefinition> beanDefinitions = new ArrayList<>(strings.size());
        for (String string : strings) {
           beanDefinitions.add(beanMap.get(string).getBeanDefinition());
        }
        return beanDefinitions;
    }

    public boolean nameExist(String name) {
        return beanMap.containsKey(name);
    }



    public void clear() {
        beanMap.clear();
        rBeanMap.clear();
    }

    @Override
    public void close() throws Exception {
        clear();
    }
}
