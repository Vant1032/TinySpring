package cc.vant.tinyspring.core.defaultpackage.annotationconfigapplicationcontext.getbean;

import cc.vant.tinyspring.core.annotations.ComponentScan;
import cc.vant.tinyspring.core.annotations.Configuration;

/**
 * @author Vant
 * @since 2018/8/15 12:39
 */
@Configuration
@ComponentScan(basePackageClasses = {SpringConfig.class})
public class SpringConfig {

}
