package cc.vant.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vant
 * @version 2018/8/12 上午 11:43
 */
public class ConfigBeanDefinition {
    Object instance;
    Map<Class<?>, Method> beanMap = new HashMap<>();

}
