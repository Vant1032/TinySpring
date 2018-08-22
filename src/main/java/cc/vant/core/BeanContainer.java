package cc.vant.core;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Vant
 * @version 2018/8/13 下午 11:18
 */
public class BeanContainer implements AutoCloseable {
    private final Map<String, BeanGenerator> beanMap = new HashMap<>();
    private final Map<String, BeanDefinition> definitionMap = new HashMap<>();
    private final Map<Class<?>, ArrayList<String>> rBeanMap = new HashMap<>();

    public void addBean(@NotNull String beanName, Class<?> clazz, BeanDefinition beanDefinition, BeanGenerator beanGenerator) {
        beanMap.put(beanName, beanGenerator);
        definitionMap.put(beanName, beanDefinition);
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

    public Set<Class<?>> getClasses() {
        return rBeanMap.keySet();
    }

    public boolean nameExist(String name) {
        return beanMap.containsKey(name);
    }



    public void clear() {
        beanMap.clear();
        rBeanMap.clear();
        definitionMap.clear();
    }

    @Override
    public void close() throws Exception {
        clear();
    }
}
