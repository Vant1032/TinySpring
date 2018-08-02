package core;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Vant
 * @version 2018/8/3 上午 12:41
 */
public class JavaConfigApplicationContext implements ApplicationContext {
    Map<String, Class> beanMap = new HashMap<>();

    public JavaConfigApplicationContext(Class config) {

    }

    /**
     * @return 如果没有找到则返回null
     */
    @Override
    public Object getBean(String beanName) {
        return beanMap.get(beanName);//
    }
}
