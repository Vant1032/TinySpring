package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.autowired.constructor;

import cc.vant.tinyspring.core.annotations.ComponentScan;
import cc.vant.tinyspring.core.annotations.Configuration;
import cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.autowired.MarkAutowired;

/**
 * @author Vant
 * @version 2018/8/10 下午 11:35
 */
@Configuration
@ComponentScan(basePackageClasses = {Pear.class, MarkAutowired.class})
public class SpringConfigConstructor {

}
