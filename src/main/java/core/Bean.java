package core;

/**
 * @author Vant
 * @version 2018/8/7 下午 2:57
 */
public class Bean {
    private Class clazz;
    private String name;
    private Object instance;

    public Bean() {
    }

    public Bean(Class clazz, String name, Object instance) {
        this.clazz = clazz;
        this.name = name;
        this.instance = instance;
    }
}