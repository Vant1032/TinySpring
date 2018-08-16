package cc.vant.core.defaultpackage.annotationconfigapplicationcontext.autowired;

import cc.vant.core.AnnotationConfigApplicationContext;
import cc.vant.core.defaultpackage.annotationconfigapplicationcontext.autowired.constructor.Pear;
import cc.vant.core.defaultpackage.annotationconfigapplicationcontext.autowired.constructor.SpringConfigConstructor;
import cc.vant.core.defaultpackage.annotationconfigapplicationcontext.autowired.onconfiguration.KiwiFruit;
import cc.vant.core.defaultpackage.annotationconfigapplicationcontext.autowired.require.Banana;
import cc.vant.core.defaultpackage.annotationconfigapplicationcontext.autowired.require.Lemon;
import cc.vant.core.defaultpackage.annotationconfigapplicationcontext.autowired.require.r2.SpringConfig2;
import cc.vant.core.exception.NoSuchBeanDefinitionException;
import cc.vant.core.defaultpackage.annotationconfigapplicationcontext.notexist.CauseWrong;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @DisplayName("Autowired作用于构造器上")
    void testAutowiredConstructor() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigConstructor.class);
        final Pear pear = context.getBean(Pear.class);
        assertNotNull(pear.getApple());
    }

    @Test
    @DisplayName("单例模式")
    void testSingleton() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigConstructor.class);
        final Pear pear = context.getBean(Pear.class);
        final Pear pear1 = context.getBean(Pear.class);
        assertSame(pear, pear1);
    }

    @Test
    @DisplayName("利用Config类的方法创建Bean")
    void testOnConfiguration() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(cc.vant.core.defaultpackage.annotationconfigapplicationcontext.autowired.onconfiguration.SpringConfig.class);
        final KiwiFruit kiwiFruit = (KiwiFruit) context.getBean("kiwiFruit");
        assertEquals(kiwiFruit.getWeight(), 1000);
    }

    @Test
    @DisplayName("require属性测试")
    void require1() {
        assertThrows(NoSuchBeanDefinitionException.class, () -> {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(cc.vant.core.defaultpackage.annotationconfigapplicationcontext.autowired.require.r1.SpringConfig.class);
            final Banana banana = (Banana) context.getBean("banana");
        });
    }

    @Test
    @DisplayName("require属性测试")
    void require2() {
        AnnotationConfigApplicationContext context2 = new AnnotationConfigApplicationContext(SpringConfig2.class);
        final Banana banana = (Banana) context2.getBean("banana");
        assertNull(banana.getMap());
    }

    @Test
    @DisplayName("require属性测试")
    void require3() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(cc.vant.core.defaultpackage.annotationconfigapplicationcontext.autowired.require.r3.SpringConfig3.class);
        final Lemon lemon = (Lemon) context.getBean("lemon");
        assertNotNull(lemon.getWatermelon());
    }
}
