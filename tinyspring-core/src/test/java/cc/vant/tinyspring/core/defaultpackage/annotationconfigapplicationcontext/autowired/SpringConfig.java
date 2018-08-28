package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.autowired;

import cc.vant.tinyspring.core.annotations.ComponentScan;
import cc.vant.tinyspring.core.annotations.Configuration;

/**
 * @author Vant
 * @since 2018/8/9 1:30
 */
@Configuration
@ComponentScan(basePackageClasses = {MarkAutowired.class})
public class SpringConfig {

}
