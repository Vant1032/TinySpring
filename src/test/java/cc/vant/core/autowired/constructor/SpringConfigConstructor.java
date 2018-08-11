package cc.vant.core.autowired.constructor;

import cc.vant.core.annotations.ComponentScan;
import cc.vant.core.annotations.Configuration;
import cc.vant.core.autowired.MarkAutowired;

/**
 * @author Vant
 * @version 2018/8/10 下午 11:35
 */
@Configuration
@ComponentScan(basePackageClasses = {Pear.class, MarkAutowired.class})
public class SpringConfigConstructor {

}
