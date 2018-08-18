package cc.vant.core.defaultpackage.annotationconfigapplicationcontext.beanname;

import cc.vant.core.AnnotationConfigApplicationContext;
import cc.vant.core.BeanContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Vant
 * @version 2018/8/18 下午 1:35
 */
public class BeanNameTest {
    @Test
    void beanNameSame() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanNameConfig.class);
        Assertions.assertEquals(BeanContainer.getBeanNames(Integer.class).size(), 2);
    }
}
