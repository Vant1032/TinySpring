package core.autowired;

import core.AnnotationConfigApplicationContext;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

/**
 * @author Vant
 * @version 2018/8/9 上午 1:32
 */
public class SpringTest {
    @Test
    void testAutowired() {
        //将会模拟autowired真实应用场景
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        final RedDeliciousApple redDeliciousApple = context.getBean(RedDeliciousApple.class);
        assertNotNull(redDeliciousApple.getApple());
    }


}
