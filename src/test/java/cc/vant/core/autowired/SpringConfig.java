package cc.vant.core.autowired;

import cc.vant.core.annotations.ComponentScan;
import cc.vant.core.annotations.Configuration;

/**
 * @author Vant
 * @version 2018/8/9 上午 1:30
 */
@Configuration
@ComponentScan(basePackageClasses = {MarkAutowired.class})
public class SpringConfig {

}
