package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.autowired;

import cc.vant.tinyspring.core.annotations.ComponentScan;
import cc.vant.tinyspring.core.annotations.Configuration;

/**
 * @author Vant
 * @version 2018/8/9 上午 1:30
 */
@Configuration
@ComponentScan(basePackageClasses = {MarkAutowired.class})
public class SpringConfig {

}
