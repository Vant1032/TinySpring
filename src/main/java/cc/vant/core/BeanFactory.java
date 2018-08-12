package cc.vant.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vant
 * @version 2018/8/12 上午 11:31
 */
public class BeanFactory {
    private Map<String, BeanDefinition> beanMap = new HashMap<>();
    private Map<Class, String> rBeanMap = new HashMap<>();

    public Object getBean(String beanName) {

    }

    public <T> T getBean(Class<T> requireType) {

    }
}
