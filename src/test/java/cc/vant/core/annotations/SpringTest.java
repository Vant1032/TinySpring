package cc.vant.core.annotations;

import cc.vant.core.AnnotationConfigApplicationContext;
import cc.vant.core.ApplicationContext;

/**
 * @author Vant
 * @version 2018/8/3 下午 11:34
 */
public class SpringTest {
    @org.junit.jupiter.api.Test
    void testConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }
}
