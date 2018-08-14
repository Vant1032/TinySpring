package cc.vant.core;

import cc.vant.core.exception.SpringInitException;
import cc.vant.core.multiconfig.SpringConfig1;
import cc.vant.core.multiconfig.SpringConfig2;
import cc.vant.core.multiconfig.SpringConfig3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vant
 * @version 2018/8/4 上午 12:18
 */
class AnnotationConfigApplicationContextTest {
    @Test
    void multiConfig() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig1.class, SpringConfig2.class);

        assertThrows(SpringInitException.class, () -> {
            AnnotationConfigApplicationContext context1 = new AnnotationConfigApplicationContext(SpringConfig1.class, SpringConfig2.class, SpringConfig3.class);
        });
    }
}