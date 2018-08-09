package core.autowired;

import core.annotations.ComponentScan;
import core.annotations.Configuration;

/**
 * @author Vant
 * @version 2018/8/9 上午 1:30
 */
@Configuration
@ComponentScan(basePackages = {MarkAutowired.class})
public class SpringConfig {

}
