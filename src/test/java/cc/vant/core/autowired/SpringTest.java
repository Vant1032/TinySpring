package cc.vant.core.autowired;

import cc.vant.core.AnnotationConfigApplicationContext;
import cc.vant.core.autowired.constructor.Pear;
import cc.vant.core.autowired.constructor.SpringConfigConstructor;
import cc.vant.core.autowired.onconfiguration.KiwiFruit;
import cc.vant.core.notexist.CauseWrong;
import cc.vant.core.exception.NoSuchBeanDefinitionException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testAutowiredNotExist() {
        //将会模拟autowired真实应用场景
        assertThrows(NoSuchBeanDefinitionException.class, () -> {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigNotExist.class);
            final CauseWrong bean = context.getBean(CauseWrong.class);
        });
    }

    @Test
    void testAutowiredConstructor() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigConstructor.class);
        final Pear pear = context.getBean(Pear.class);
        assertNotNull(pear.getApple());
    }

    @Test
    void testSingleton() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigConstructor.class);
        final Pear pear = context.getBean(Pear.class);
        final Pear pear1 = context.getBean(Pear.class);
        assertSame(pear, pear1);
    }

    @Test
    void testOnConfiguration() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(cc.vant.core.autowired.onconfiguration.SpringConfig.class);
        final KiwiFruit kiwiFruit = (KiwiFruit) context.getBean("kiwiFruit");
        assertEquals(kiwiFruit.getWeight(), 1000);
    }
}
