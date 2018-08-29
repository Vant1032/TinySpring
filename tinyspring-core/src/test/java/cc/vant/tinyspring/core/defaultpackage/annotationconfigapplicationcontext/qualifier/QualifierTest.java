package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.qualifier;

import cc.vant.tinyspring.core.AnnotationConfigApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Vant
 * @since 2018/8/22 16:19
 */

class QualifierTest {
    @Test
    void test() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(IceCreamConfig.class);
        Consume bean = context.getBean(Consume.class);
        Assertions.assertEquals(bean.getIceCream().getSize(), 1000);
    }
}
