package cc.vant.core;

/**
 * @author Vant
 * @version 2018/8/7 下午 2:57
 */
public class BeanDefinition {
    Class clazz;
    String name;
    Object instance;
    BeanWiredData wiredData;

    public BeanDefinition() {
    }

    public BeanDefinition(Class clazz, String name, Object instance, BeanWiredData wiredData) {
        this.clazz = clazz;
        this.name = name;
        this.instance = instance;
        this.wiredData = wiredData;
    }
}