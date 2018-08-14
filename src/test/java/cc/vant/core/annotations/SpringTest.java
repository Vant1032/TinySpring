package cc.vant.core.annotations;

import cc.vant.core.AnnotationConfigApplicationContext;

/**
 * @author Vant
 * @version 2018/8/3 下午 11:34
 */
public class SpringTest {
    @org.junit.jupiter.api.Test
    void testConfig() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }
}
