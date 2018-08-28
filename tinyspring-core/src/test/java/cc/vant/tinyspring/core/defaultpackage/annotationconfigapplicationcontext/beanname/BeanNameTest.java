package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.beanname;

import cc.vant.tinyspring.core.AnnotationConfigApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Vant
 * @since 2018/8/18 13:35
 */
public class BeanNameTest {
    @Test
    void beanNameSame() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanNameConfig.class);
        Assertions.assertEquals(context.getBeanNames(Integer.class).size(), 2);
    }
}
