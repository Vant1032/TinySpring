package core.autowired;

import core.annotations.ComponentScan;
import core.annotations.Configuration;
import core.autowired.constructor.Pear;

/**
 * @author Vant
 * @version 2018/8/10 下午 11:35
 */
@Configuration
@ComponentScan(basePackageClasses = {Pear.class, MarkAutowired.class})
public class SpringConfigConstructor {

}
