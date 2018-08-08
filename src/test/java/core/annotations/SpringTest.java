package core.annotations;

import core.AnnotationConfigApplicationContext;
import core.ApplicationContext;

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
