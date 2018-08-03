package core.annotations;

import core.AnnotationConfigApplicationContext;
import core.ApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * @author Vant
 * @version 2018/8/3 下午 11:34
 */
public class JavaConfigTest {
    @Test
    public void testConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }
}
