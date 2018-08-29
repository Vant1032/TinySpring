package cc.vant.tinyspring.core.defaultpackage;

import cc.vant.tinyspring.core.AnnotationConfigApplicationContext;
import cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.getbean.AFather;
import cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.getbean.IFather;
import cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.getbean.SpringConfig;
import cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.multiconfig.SpringConfig1;
import cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.multiconfig.SpringConfig2;
import cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.multiconfig.SpringConfig3;
import cc.vant.tinyspring.core.exception.SpringInitException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vant
 * @since 2018/8/4 12:18
 */
class AnnotationConfigApplicationContextTest {
    @Test
    void multiConfig() {
        //noinspection unused
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig1.class, SpringConfig2.class);

        assertThrows(SpringInitException.class, () -> {
            AnnotationConfigApplicationContext context1 = new AnnotationConfigApplicationContext(SpringConfig1.class, SpringConfig2.class, SpringConfig3.class);
        });
    }

    @Test
    void abstractBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        final IFather father = context.getBean(IFather.class);
        father.foo();
        final AFather bean = context.getBean(AFather.class);
        bean.bar();
    }
}