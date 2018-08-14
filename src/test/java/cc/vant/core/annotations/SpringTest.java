package cc.vant.core.annotations;

import cc.vant.core.AnnotationConfigApplicationContext;

/**
 * @author Vant
 * @version 2018/8/3 下午 11:34
 */
public class SpringTest {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("根据Config类创建Context")
    void testConfig() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }
}
